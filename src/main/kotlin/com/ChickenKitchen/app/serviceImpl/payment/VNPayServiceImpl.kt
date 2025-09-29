package com.ChickenKitchen.app.serviceImpl.payment

import com.ChickenKitchen.app.config.VNPayConfig
import com.ChickenKitchen.app.enum.PaymentMethodType
import com.ChickenKitchen.app.enum.TransactionType
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.handler.PaymentException
import com.ChickenKitchen.app.handler.PaymentMethodNotActiveException
import com.ChickenKitchen.app.handler.PaymentMethodNotFoundException
import com.ChickenKitchen.app.model.entity.transaction.Transaction
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.payment.TransactionRepository
import com.ChickenKitchen.app.service.payment.PaymentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


@Service
class VNPayServiceImpl (
    private val orderRepository: OrderRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val transactionRepository: TransactionRepository,
    private val vnPayConfig: VNPayConfig,
) : PaymentService {


    override fun createURLService(
        orderId: Long,
        paymentMethod: PaymentMethodType
    ): String {

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order not found with id $orderId") }

        if (paymentMethod == PaymentMethodType.CASH_ON_DELIVERY) {
            throw PaymentMethodNotFoundException("COD not support online payment")
        }

        val paymentMethodEntity = paymentMethodRepository.findByName(paymentMethod)
            .orElseThrow { PaymentMethodNotFoundException("Unsupported payment method: $paymentMethod") }


        if (!paymentMethodEntity.isActive) {
            throw PaymentMethodNotActiveException("Payment method $paymentMethod is currently not available")
        }

        val amountVnd = convertUsdToVnd(order.totalPrice)
        val orderInfo = "Payment for order $orderId ${order.user.id}"
        val vnpTxnRef = VNPayConfig.getRandomNumber(8)

        val vnpParams = mutableMapOf(
            "vnp_Version" to vnPayConfig.vnpVersion,
            "vnp_Command" to vnPayConfig.vnpCommand,
            "vnp_TmnCode" to vnPayConfig.vnpTmnCode,
            "vnp_Amount" to (amountVnd * 100).toString(),
            "vnp_BankCode" to "VNBANK",
            "vnp_CurrCode" to "VND",
            "vnp_TxnRef" to vnpTxnRef,
            "vnp_OrderInfo" to orderInfo,
            "vnp_OrderType" to vnPayConfig.vnpOrderType,
            "vnp_Locale" to "vn",
            "vnp_ReturnUrl" to "${vnPayConfig.vnpReturnUrl}$orderId = {orderId}",
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


    @Transactional
    override fun callBack(params: Map<String, String>): String {

//        // 1. Verify hash
//        val vnpSecureHash = params["vnp_SecureHash"] ?: throw PaymentException("Hash không tồn tại")
//        val filteredFields = params.filterKeys { it != "vnp_SecureHash" && it != "vnp_SecureHashType" && !params[it].isNullOrEmpty() }
//        val sortedKeys = filteredFields.keys.sorted()
//        val hashData = sortedKeys.joinToString("&") { "$it=${filteredFields[it]}" }
//        val calculatedHash = VNPayConfig.hmacSHA512(vnPayConfig.vnpHashSecret, hashData)
//
//        if (!calculatedHash.equals(vnpSecureHash, ignoreCase = true)) {
//            throw PaymentException("Hash không hợp lệ")
//        }
// Tạm thời  không verify để xem luồng cập nhật trạng thái đơn hàng

        // 2. Lấy orderId từ returnUrl param
        val orderId = params["orderId"]?.toLongOrNull()
            ?: throw PaymentException("Không tìm thấy orderId trong callback")

        val responseCode = params["vnp_ResponseCode"] ?: throw PaymentException("Không có response code từ VNPAY")
        if (responseCode != "00") {
            throw PaymentException("Thanh toán thất bại (code: $responseCode)")
        }

        val order = orderRepository.findById(orderId)
            .orElseThrow { PaymentException("Không tìm thấy order $orderId") }

        val paymentMethod = paymentMethodRepository.findByName(PaymentMethodType.VNPay)
            .orElseThrow { PaymentMethodNotFoundException("VNPay method not found") }

        val user = order.user

        val newTransaction = Transaction(
            order = order,
            user = user,
            paymentMethod = paymentMethod,
            transactionType = TransactionType.SUCCESS,
            amount = order.totalPrice,
            createdAt = java.sql.Timestamp(System.currentTimeMillis()),
            note = "VNPAY Payment Initiated - BankTranNo: ${params["vnp_BankTranNo"]}"
        )
        transactionRepository.save(newTransaction)
        return "Payment Success"
    }
}