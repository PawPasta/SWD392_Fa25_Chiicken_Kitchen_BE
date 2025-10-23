package com.ChickenKitchen.app.serviceImpl.payment

import com.ChickenKitchen.app.config.VNPayConfig
import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.enums.PaymentStatus
import com.ChickenKitchen.app.enums.TransactionStatus
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.model.entity.payment.Transaction
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.payment.PaymentRepository
import com.ChickenKitchen.app.repository.payment.TransactionRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.repository.user.WalletRepository
import com.ChickenKitchen.app.service.payment.VNPayService
import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


@Service
class VNPayServiceImpl (
    private val vnPayConfig: VNPayConfig,
    private val transactionRepository: TransactionRepository,
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) : VNPayService {

    @Transactional
    override fun createVnPayURL (orderId: Long): String {
        val payment = paymentRepository.findByOrderId(orderId)
        val orderInfo = "Payment for order $orderId ${payment?.user?.id}"
        val vnpTxnRef = VNPayConfig.getRandomNumber(8)

        val vnpParams = mutableMapOf(
            "vnp_Version" to vnPayConfig.vnpVersion,
            "vnp_Command" to vnPayConfig.vnpCommand,
            "vnp_TmnCode" to vnPayConfig.vnpTmnCode,
            "vnp_Amount" to ((payment?.finalAmount ?: 0) * 100).toString(),
            "vnp_BankCode" to "VNBANK",
            "vnp_CurrCode" to "VND",
            "vnp_TxnRef" to vnpTxnRef,
            "vnp_OrderInfo" to orderInfo,
            "vnp_OrderType" to vnPayConfig.vnpOrderType,
            "vnp_Locale" to "vn",
            "vnp_ReturnUrl" to "${vnPayConfig.vnpReturnUrl}?orderId=${orderId}",
            "vnp_IpAddr" to vnPayConfig.vnpIpAddr
        )

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))
        val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
        vnpParams["vnp_CreateDate"] = formatter.format(calendar.time)

        calendar.add(Calendar.MINUTE, 15)
        vnpParams["vnp_ExpireDate"] = formatter.format(calendar.time)

        val fieldNames = vnpParams.keys.sorted()
        val hashData = fieldNames.joinToString("&") { "$it=${URLEncoder.encode(vnpParams[it], "UTF-8")}" }
        val query = fieldNames.joinToString("&") { "${URLEncoder.encode(it, "UTF-8")}=${URLEncoder.encode(vnpParams[it], "UTF-8")}" }

        val vnpSecureHash = VNPayConfig.hmacSHA512(vnPayConfig.vnpHashSecret, hashData)
        return "${vnPayConfig.vnpPayUrl}?$query&vnp_SecureHash=$vnpSecureHash"
    }


    override fun callbackURL(params: Map<String, String>): String {


        val responseCode = params["vnp_ResponseCode"] ?: return "Missing response code"
        val orderId = params["orderId"]?.toLongOrNull() ?: return "Invalid order ID"
        val transactionStatus = params["vnp_TransactionStatus"]
        val secureHash = params["vnp_SecureHash"]
        val vnpAmount = params["vnp_Amount"]?.toLongOrNull()?.div(100)
        val vnpTxnRef = params["vnp_TxnRef"]
        val vnpBankCode = params["vnp_BankCode"]
        val vnpTransactionNo = params["vnp_TransactionNo"]
        val vnpPayDate = params["vnp_PayDate"]

        // Resolve order and use its owner as the authoritative user for callback
        val order = orderRepository.findById(orderId.toLong())
            .orElseThrow { OrderNotFoundException("Order not found with id $orderId") }

        val payment = order.payment
            ?: throw OrderNotFoundException("Payment not found for order $orderId")

        val user = order.user
        val wallet = walletRepository.findByUser(user)
            ?: throw OrderNotFoundException("Wallet not found for user ${user.id}")

        val paymentMethod = paymentMethodRepository.findByName("VNPay")

        return when (responseCode) {
            "00" -> {
                // 1. Cập nhật trạng thái payment và order
                payment.status = PaymentStatus.FINISHED
                order.status = OrderStatus.CONFIRMED
                orderRepository.save(order)

                // 2. Tạo transaction nạp tiền vào ví
                val depositTransaction = Transaction(
                    payment = payment,
                    wallet = wallet,
                    paymentMethod = paymentMethod,
                    transactionType = TransactionStatus.DEBIT,
                    amount = payment.finalAmount,
                    note = "Deposit via VNPAY"
                )
                transactionRepository.save(depositTransaction)

                // 3. Tạo transaction thanh toán đơn hàng từ ví
                val paymentTransaction = Transaction(
                    payment = payment,
                    wallet = wallet,
                    paymentMethod = paymentMethod,
                    transactionType = TransactionStatus.CREDIT,
                    amount = payment.finalAmount,
                    note = "Payment for order ${order.id} via wallet"
                )
                transactionRepository.save(paymentTransaction)

                return "Payment success and transactions created"
            }
            "24" -> {
                payment.status = PaymentStatus.CANCELLED
                order.status = OrderStatus.FAILED
                orderRepository.save(order)
                paymentRepository.save(payment)
                "Payment Failed"
            }
            else ->{
                "Payment failed"
            }
        }
    }
}
