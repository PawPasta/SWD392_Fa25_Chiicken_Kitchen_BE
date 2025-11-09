package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.OrderConfirmRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.order.OrderPaymentService
import com.ChickenKitchen.app.service.payment.MomoService
import com.ChickenKitchen.app.service.payment.VNPayService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class OrderPaymentController(
    private val orderPaymentService: OrderPaymentService,
    private val vnPayService: VNPayService,
    private val momoService: MomoService
) {
    @Operation(summary = "Confirm order (initiate payment and return provider URL)")
    @PostMapping("/api/orders/confirm")
    fun confirmOrder(@RequestBody req: OrderConfirmRequest): ResponseEntity<ResponseModel> {
        val url = orderPaymentService.confirmOrder(req)
        return ResponseEntity.ok(ResponseModel.success(url, "Confirmed on going"))
    }

    @Operation(summary = "VNPay callback")
    @PostMapping("/api/payments/vnpay/callback")
    fun vnpayCallback(@RequestBody params: Map<String, String>): ResponseEntity<ResponseModel> {
        val msg = vnPayService.callbackURL(params)
        return ResponseEntity.ok(ResponseModel.success(msg, "Nice"))
    }

    @Operation(summary = "Momo callback")
    @PostMapping("/api/payments/momo/callback")
    fun momoCallback(@RequestBody params: Map<String, String>): ResponseEntity<ResponseModel> {
        val msg = momoService.callBack(params)
        return ResponseEntity.ok(ResponseModel.success(msg, "Nice"))
    }
    //Cái này chỉ để test momo sandbox với amount tùy ý, không cần order
    @Operation(summary = "Test MoMo sandbox with custom amount (no order required)")
    @PostMapping("/api/payments/momo/test")
    fun momoTest(@RequestParam amount: Long): ResponseEntity<ResponseModel> {
        val payUrl = momoService.createMomoURLTest(amount)
        return ResponseEntity.ok(ResponseModel.success(payUrl, "Sandbox payment URL created"))
    }

    @Operation(summary = "MoMo IPN (server-to-server, auto called by MoMo)")
    @PostMapping("/api/payments/momo/ipn")
    fun momoIpn(@RequestBody payload: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        val result = momoService.callBack(payload)
        return ResponseEntity.ok(mapOf("message" to result))
    }

    @Operation(summary = "VNPay IPN (server-to-server, auto-called by VNPay)")
    @GetMapping("/api/payments/vnpay/ipn")
    fun vnpayIpn(@RequestParam params: Map<String, String>): ResponseEntity<String> {
        val result = vnPayService.callbackURL(params)
        return ResponseEntity.ok(result)
    }
}

