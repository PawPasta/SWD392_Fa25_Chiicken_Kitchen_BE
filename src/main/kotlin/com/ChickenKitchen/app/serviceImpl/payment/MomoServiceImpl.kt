package com.ChickenKitchen.app.serviceImpl.payment

import com.ChickenKitchen.app.config.MomoConfig
import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.enums.PaymentStatus
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.model.dto.request.SingleNotificationRequest
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.payment.PaymentRepository
import com.ChickenKitchen.app.service.notification.NotificationService
import com.ChickenKitchen.app.service.payment.MomoService
import com.ChickenKitchen.app.service.payment.TransactionService
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class MomoServiceImpl(
    private val momoConfig: MomoConfig,
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val transactionService: TransactionService,
    private val notificationService: NotificationService,
    private val restTemplate: RestTemplate
) : MomoService {

    companion object {
        private const val DEFAULT_REQUEST_TYPE = "captureWallet"
        private const val DEFAULT_STORE_ID = "ChickenKitchenStore"
    }

    override fun createMomoURL(order: Order, channel: String?): String {
        val payment = paymentRepository.findByOrderId(
            order.id ?: throw OrderNotFoundException("Payment for order ${order.id} not found")
        )

        val amount = payment?.finalAmount ?: 0L
        val orderInfo = "Payment for order ${order.id}"

        // 🔒 Tạo orderId “an toàn” (MoMo yêu cầu chuỗi)
        val safeOrderId = "CK-${UUID.randomUUID().toString().substring(0, 8)}-${order.id}"

        val redirectUrl = if (channel?.uppercase() == "APP")
            momoConfig.appRedirectUrl
        else
            momoConfig.redirectUrl

        return createMomoURLInternal(
            orderId = safeOrderId,
            amount = amount.toLong(),
            orderInfo = orderInfo,
            partnerName = "Chicken Kitchen",
            redirectUrl = redirectUrl
        )
    }

    override fun createMomoURLTest(amount: Long): String {
        val orderInfo = "Test MoMo payment - amount $amount"
        return createMomoURLInternal(
            orderId = "TEST-${UUID.randomUUID()}",
            amount = amount,
            orderInfo = orderInfo,
            partnerName = "Chicken Kitchen (Test)",
            storeId = "ChickenKitchenSandbox",
            requestType = DEFAULT_REQUEST_TYPE,
            redirectUrl = momoConfig.redirectUrl
        )
    }

    private fun createMomoURLInternal(
        orderId: String,
        amount: Long,
        orderInfo: String,
        partnerName: String,
        storeId: String = DEFAULT_STORE_ID,
        requestType: String = DEFAULT_REQUEST_TYPE,
        redirectUrl: String
    ): String {
        val requestId = UUID.randomUUID().toString()
        val extraData = ""

        val raw = listOf(
            "accessKey=${momoConfig.accessKey}",
            "amount=$amount",
            "extraData=$extraData",
            "ipnUrl=${momoConfig.ipnUrl}",
            "orderId=$orderId",
            "orderInfo=$orderInfo",
            "partnerCode=${momoConfig.partnerCode}",
            "redirectUrl=$redirectUrl",
            "requestId=$requestId",
            "requestType=$requestType"
        ).joinToString("&")

        val signature = momoConfig.sign(raw)

        val body = mapOf(
            "partnerCode" to momoConfig.partnerCode,
            "partnerName" to partnerName,
            "storeId" to storeId,
            "requestType" to requestType,
            "ipnUrl" to momoConfig.ipnUrl,
            "redirectUrl" to redirectUrl,
            "orderId" to orderId,
            "amount" to amount,
            "lang" to momoConfig.lang,
            "orderInfo" to orderInfo,
            "requestId" to requestId,
            "extraData" to extraData,
            "signature" to signature
        )

        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity(body, headers)

        println("📤 [MoMo] Sending request to ${momoConfig.endpoint}")
        val response = restTemplate.postForEntity(momoConfig.endpoint, entity, Map::class.java)
        val responseBody = response.body ?: throw RuntimeException("Empty response from MoMo")

        println("📥 [MoMo] Response: $responseBody")

        return responseBody["payUrl"]?.toString()
            ?: throw RuntimeException("Missing payUrl: $responseBody")
    }

    /**
     * ✅ MoMo IPN callback (Server → Server)
     * Khi thanh toán xong, MoMo sẽ tự động gọi vào ipnUrl (bên server bạn).
     */
    override fun callBack(params: Map<String, Any>): String {

        val resultCode = params["resultCode"]?.toString() ?: return "Missing result code"
        val momoOrderId = params["orderId"]?.toString() ?: return "Missing order id"
        val message = params["message"]?.toString() ?: ""
        val transId = params["transId"]?.toString() ?: "N/A"


        val orderIdStr = momoOrderId.substringAfterLast("-")
        val orderId = orderIdStr.toLongOrNull()
            ?: throw RuntimeException("Invalid orderId format from MoMo: $momoOrderId")

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order not found: $orderId") }

        val payment = order.payment ?: throw OrderNotFoundException("Payment not found for order $orderId")
        val paymentMethod = paymentMethodRepository.findByName("MoMo")
            ?: throw OrderNotFoundException("Payment method MoMo not found")

        return try {
            if (resultCode == "0") {
                println("✅ [MoMo IPN] Payment SUCCESS for order $orderId (transId=$transId)")
                payment.status = PaymentStatus.FINISHED
                order.status = OrderStatus.CONFIRMED

                paymentRepository.save(payment)
                orderRepository.save(order)

                transactionService.createPaymentTransaction(payment, order, paymentMethod)

                // 🚀 Gửi push notification về app
                notificationService.sendToUser(
                    SingleNotificationRequest(
                        user = order.user,
                        title = "Payment Successful",
                        body = "Your MoMo payment for order #${order.id} was successful.",
                    )
                )
                "Payment success for order $orderId"
            } else {
                println("❌ [MoMo IPN] Payment FAILED for order $orderId (resultCode=$resultCode, msg=$message)")
                payment.status = PaymentStatus.PENDING
                order.status = OrderStatus.FAILED
                paymentRepository.save(payment)
                orderRepository.save(order)

                notificationService.sendToUser(
                    SingleNotificationRequest(
                        user = order.user,
                        title = "Payment Failed",
                        body = "Your MoMo payment for order #${order.id} failed: $message",
                    )
                )
                "Payment failed for order $orderId: $message"
            }
        } catch (e: Exception) {
            println("💥 [MoMo IPN] Error processing callback: ${e.message}")
            e.printStackTrace()
            "Internal error processing MoMo callback"
        }
    }
}
