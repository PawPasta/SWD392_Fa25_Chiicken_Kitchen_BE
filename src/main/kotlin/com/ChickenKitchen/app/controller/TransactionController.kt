package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.transaction.PaymentMethodService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/transaction")
class TransactionController (
    private val paymentMethodService: PaymentMethodService
)
{

    @Operation(summary = "Get all payment Method")
    @GetMapping("/payment-method")
    fun getAllPaymentMethod() : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.getAll(), "Get all payment method successfully"))
    }

    @Operation(summary = "Get Payment Method By Id")
    @GetMapping("/payment-method/{id}")
    fun getPaymentMethodById(@PathVariable id : Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.getById(id),"Get Payment Method with $id successfully"))
    }

    @Operation(summary = "Create payment method")
    @PostMapping("/payment-method")
    fun createPaymentMethod (@RequestBody req: CreatePaymentMethodRequest) : ResponseEntity<ResponseModel>{
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.create(req), "Create Payment Method Successfully"))
    }

    @Operation(summary = "update payment method")
    @PostMapping("/payment-method/{id}")
    fun updatePaymentMethod (@PathVariable id : Long, @RequestBody req : UpdatePaymentMethodRequest) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.update(id, req), "Updated Payment Method Successfully"))
    }

    @Operation
    @PatchMapping("/payment-method/{id}")
    fun updatePaymentMethodStatus (@PathVariable id : Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.changeStatus(id),"Updated Status Successfully"))
    }



}