package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.OrderConfirmRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.order.OrderPaymentService
import com.ChickenKitchen.app.service.payment.VNPayService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class OrderPaymentController(
    private val orderPaymentService: OrderPaymentService,
    private val vnPayService: VNPayService
) {
    @Operation(summary = "Confirm order (initiate payment and return provider URL)")
    @PostMapping("/api/orders/confirm")
    fun confirmOrder(@RequestBody req: OrderConfirmRequest): ResponseEntity<ResponseModel> {
        val url = orderPaymentService.confirmOrder(req)
        return ResponseEntity.ok(ResponseModel.success(url, "Confirmed on going"))
    }

    // Keep backward-compatible callback path
    @Operation(summary = "VNPay callback (legacy path)")
    @PostMapping("/api/orders/vnpay-callback")
    fun vnpayCallbackLegacy(@RequestBody params: Map<String, String>): ResponseEntity<ResponseModel> {
        val msg = vnPayService.callbackURL(params)
        return ResponseEntity.ok(ResponseModel.success(msg, "Nice"))
    }

    // New structured callback path
    @Operation(summary = "VNPay callback")
    @PostMapping("/api/payments/vnpay/callback")
    fun vnpayCallback(@RequestBody params: Map<String, String>): ResponseEntity<ResponseModel> {
        val msg = vnPayService.callbackURL(params)
        return ResponseEntity.ok(ResponseModel.success(msg, "Nice"))
    }
}

