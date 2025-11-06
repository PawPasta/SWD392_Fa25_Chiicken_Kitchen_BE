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

    private companion object {
        const val DEFAULT_REQUEST_TYPE = "captureWallet"
        const val DEFAULT_STORE_ID = "ChickenKitchenStore"
    }

    override fun createMomoURL(order: Order, channel: String?): String {
        val payment = paymentRepository.findByOrderId(
            order.id ?: throw OrderNotFoundException("Payment with order id ${order.id} not found")
        )

        val amount = payment?.finalAmount ?: 0L
        val orderInfo = "Payment for order ${order.id}"

        //  Phải làm bước này vì sao ư
        // Tại anh momo khá là khó tính, nếu như orderId trong db có dạng Long khi truyền vào thì sẽ không được chấp nhận
        // Nếu như đã dùng UUID thì không sao, nhưng nếu đã lỡ làm Long rồi thì phải ma đạo id sao cho nó thành chuỗi thi` momo moi chap nhan
        val safeOrderId = "CK-${UUID.randomUUID().toString().substring(0, 8)}-${order.id}"

        // ✅ Chọn redirectUrl theo channel
        val redirectUrl = if (channel?.uppercase() == "APP") momoConfig.appRedirectUrl else momoConfig.redirectUrl

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

        val raw = buildRawSignature(
            accessKey = momoConfig.accessKey,
            amount = amount,
            extraData = extraData,
            ipnUrl = momoConfig.ipnUrl,
            orderId = orderId,
            orderInfo = orderInfo,
            partnerCode = momoConfig.partnerCode,
            redirectUrl = redirectUrl,
            requestId = requestId,
            requestType = requestType
        )

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

        return postAndExtractPayUrl(body)
    }

    private fun buildRawSignature(
        accessKey: String,
        amount: Long,
        extraData: String,
        ipnUrl: String,
        orderId: String,
        orderInfo: String,
        partnerCode: String,
        redirectUrl: String,
        requestId: String,
        requestType: String
    ): String {
        return listOf(
            "accessKey=$accessKey",
            "amount=$amount",
            "extraData=$extraData",
            "ipnUrl=$ipnUrl",
            "orderId=$orderId",
            "orderInfo=$orderInfo",
            "partnerCode=$partnerCode",
            "redirectUrl=$redirectUrl",
            "requestId=$requestId",
            "requestType=$requestType"
        ).joinToString("&")
    }

    private fun postAndExtractPayUrl(requestBody: Map<String, Any>): String {
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity(requestBody, headers)

        val response = restTemplate.postForEntity(momoConfig.endpoint, entity, Map::class.java)
        val body = response.body ?: throw RuntimeException("Empty response from MoMo")

        return body["payUrl"]?.toString() ?: throw RuntimeException("Missing payUrl: $body")
    }

    override fun callBack(params: Map<String, Any>): String {
        val resultCode = params["resultCode"]?.toString() ?: return "Missing result code"
        val momoOrderId = params["orderId"]?.toString() ?: return "Missing order id"
        val message = params["message"]?.toString() ?: ""

        // ✅ Tách ra order.id thật từ chuỗi "CK-xxxxxxx-<id>"
        val orderIdStr = momoOrderId.substringAfterLast("-")
        val orderId = orderIdStr.toLongOrNull()
            ?: throw RuntimeException("Invalid order id format in momoOrderId: $momoOrderId")

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order not found with id $orderId") }

        val payment = order.payment ?: throw OrderNotFoundException("Payment not found for order $orderId")
        val paymentMethod = paymentMethodRepository.findByName("MoMo")
            ?: throw OrderNotFoundException("Payment method MoMo not found")

        return if (resultCode == "0") {
            payment.status = PaymentStatus.FINISHED
            order.status = OrderStatus.CONFIRMED
            paymentRepository.save(payment)
            orderRepository.save(order)
            transactionService.createPaymentTransaction(payment, order, paymentMethod)

            notificationService.sendToUser(
                SingleNotificationRequest(
                    user = order.user,
                    title = "Payment Successful",
                    body = "Your MoMo payment for order ${order.id} was successful."
                )
            )

            "Payment successful and transaction created"
        } else {
            payment.status = PaymentStatus.PENDING
            order.status = OrderStatus.FAILED
            paymentRepository.save(payment)
            orderRepository.save(order)

            notificationService.sendToUser(
                SingleNotificationRequest(
                    user = order.user,
                    title = "Payment Failed",
                    body = "Your payment for order ${order.id} using MoMo has failed."
                )
            )
            "Payment failed: $message"
        }
    }
}
