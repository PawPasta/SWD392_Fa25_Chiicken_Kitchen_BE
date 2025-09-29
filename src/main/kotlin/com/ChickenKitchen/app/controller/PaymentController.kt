package com.ChickenKitchen.app.controller


import com.ChickenKitchen.app.enum.PaymentMethodType
import com.ChickenKitchen.app.model.dto.response.ResponseModel

import com.ChickenKitchen.app.serviceImpl.payment.VNPayServiceImpl
import io.swagger.v3.oas.annotations.Operation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController



@RestController
@RequestMapping("/api/payments")
class PaymentController (
    private val vnPayServiceImpl: VNPayServiceImpl
) {

    @Operation(summary = "Create VNPAY payment URL")
    @PostMapping("/create-vnpay-url/{orderId}")
    fun createVNPAYURL (@PathVariable orderId :  Long) : ResponseEntity<ResponseModel> {
        val vnpay = vnPayServiceImpl.createURLService(orderId, PaymentMethodType.VNPay)
        return ResponseEntity.ok(ResponseModel.success(vnpay, "Create VNPAY URL successfully!"))
    }

    @Operation(summary = "VNPAY verify payment")
    @PostMapping("/vnpay-payment-result")
    fun vnpayCallBack(@RequestBody params: Map<String,String>): ResponseEntity<ResponseModel> {
        val result = vnPayServiceImpl.callBack(params)
        return ResponseEntity.ok(ResponseModel.success(result, "VNPAY callback successfully!"))
    }

}