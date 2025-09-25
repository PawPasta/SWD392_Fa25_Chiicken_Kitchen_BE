package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.payment.PaymentMethodService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment-method")
class PaymentMethodController(
    private val paymentMethodService: PaymentMethodService
) {

    // Public
    @Operation(summary = "Get all payment methods")
    @GetMapping("")
    fun getAllPaymentMethods(): ResponseEntity<ResponseModel> {
        val methods = paymentMethodService.getAll()
        return ResponseEntity.ok(ResponseModel.success(methods, "Get all payment methods successfully!"))
    }

    @Operation(summary = "Get payment method by ID")
    @GetMapping("/{id}")
    fun getPaymentMethodById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val method = paymentMethodService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(method, "Get payment method successfully!"))
    }

    // Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new payment method (Admin only)")
    @PostMapping("")
    fun createPaymentMethod(@RequestBody req: CreatePaymentMethodRequest): ResponseEntity<ResponseModel> {
        val method = paymentMethodService.create(req)
        return ResponseEntity.ok(ResponseModel.success(method, "Create payment method successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update payment method by ID (Admin only)")
    @PutMapping("/{id}")
    fun updatePaymentMethod(@PathVariable id: Long, @RequestBody req: UpdatePaymentMethodRequest): ResponseEntity<ResponseModel> {
        val method = paymentMethodService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(method, "Update payment method successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete payment method by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deletePaymentMethod(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        paymentMethodService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete payment method successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change payment method status (Admin only)")
    @PatchMapping("/{id}/status")
    fun changePaymentMethodStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val method = paymentMethodService.changeStatus(id)
        return ResponseEntity.ok(ResponseModel.success(method, "Change payment method status successfully!"))
    }
}

