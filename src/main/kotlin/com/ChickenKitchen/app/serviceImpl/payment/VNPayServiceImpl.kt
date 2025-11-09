package com.ChickenKitchen.app.serviceImpl.payment

import com.ChickenKitchen.app.config.VNPayConfig
import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.enums.PaymentStatus
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.model.dto.request.SingleNotificationRequest
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.payment.PaymentRepository
import com.ChickenKitchen.app.service.notification.NotificationService
import com.ChickenKitchen.app.service.payment.TransactionService
import com.ChickenKitchen.app.service.payment.VNPayService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

@Service
class VNPayServiceImpl(
    private val vnPayConfig: VNPayConfig,
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val transactionService: TransactionService,
    private val notificationService: NotificationService
) : VNPayService {

    @Transactional
    override fun createVnPayURL(order: Order, channel: String?): String {
        val payment = paymentRepository.findByOrderId(order.id
            ?: throw OrderNotFoundException("Payment with order id ${order.id} not found"))
        val orderInfo = "Payment for order ${order.id}"
        val vnpTxnRef = VNPayConfig.getRandomNumber(8)

        val returnUrlBase = when (channel?.lowercase()) {
            "app" -> vnPayConfig.vnpAppReturnUrl
            else -> vnPayConfig.vnpReturnUrl
        }

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
            "vnp_ReturnUrl" to "$returnUrlBase?orderId=${order.id}",
            "vnp_IpnUrl" to "${vnPayConfig.vnpIpnUrl}?orderId=${order.id}",
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
        try {
            println("📥 [VNPay Callback/IPN] Received: $params")


            val vnpSecureHash = params["vnp_SecureHash"] ?: return "RspCode=97&Message=MissingSecureHash"
            val paramsWithoutHash = params.toMutableMap().apply {
                remove("vnp_SecureHash")
                remove("vnp_SecureHashType")
            }

            val sortedKeys = paramsWithoutHash.keys.sorted()
            val data = sortedKeys.joinToString("&") { "$it=${paramsWithoutHash[it]}" }
            val calculatedHash = VNPayConfig.hmacSHA512(vnPayConfig.vnpHashSecret, data)

            if (!calculatedHash.equals(vnpSecureHash, ignoreCase = true)) {
                return "RspCode=97&Message=Invalid checksum"
            }


            val responseCode = params["vnp_ResponseCode"] ?: return "RspCode=99&Message=Missing response code"
            val transactionStatus = params["vnp_TransactionStatus"]
            val vnpTxnRef = params["vnp_TxnRef"]
            val orderId = params["orderId"]?.toLongOrNull()
            val amount = params["vnp_Amount"]?.toLongOrNull()?.div(100)

            if (orderId == null) return "RspCode=99&Message=Invalid orderId"

            val order = orderRepository.findById(orderId)
                .orElseThrow { OrderNotFoundException("Order not found with id $orderId") }

            val payment = order.payment ?: throw OrderNotFoundException("Payment not found for order $orderId")
            val paymentMethod = paymentMethodRepository.findByName("VNPay")
                ?: throw OrderNotFoundException("Payment method VNPay not found")


            return when (responseCode) {
                "00" -> {
                    payment.status = PaymentStatus.FINISHED
                    order.status = OrderStatus.CONFIRMED
                    paymentRepository.save(payment)
                    orderRepository.save(order)
                    transactionService.createPaymentTransaction(payment, order, paymentMethod)

                    notificationService.sendToUser(
                        SingleNotificationRequest(
                            user = order.user,
                            title = "Payment Successful",
                            body = "Your VNPay payment for order ${order.id} was successful."
                        )
                    )

                    "RspCode=00&Message=Confirm Success"
                }
                else -> {
                    payment.status = PaymentStatus.PENDING
                    order.status = OrderStatus.FAILED
                    paymentRepository.save(payment)
                    orderRepository.save(order)

                    notificationService.sendToUser(
                        SingleNotificationRequest(
                            user = order.user,
                            title = "Payment Failed",
                            body = "Your VNPay payment for order ${order.id} has failed."
                        )
                    )

                    "RspCode=00&Message=Confirm Failed"
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            return "RspCode=99&Message=Unknown error"
        }
    }
}
