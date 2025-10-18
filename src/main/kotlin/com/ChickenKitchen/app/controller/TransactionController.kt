package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.transaction.PaymentMethodService
import com.ChickenKitchen.app.service.transaction.TransactionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/transaction")
class TransactionController (
    private val paymentMethodService: PaymentMethodService,
    private val transactionService: TransactionService,
)
{

    @Operation(summary = "Get all payment Method (all actor)")
    @GetMapping("/payment-method")
    fun getAllPaymentMethod() : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.getAll(), "Get all payment method successfully"))
    }

    @Operation(summary = "Get Payment Method By Id (all actor)")
    @GetMapping("/payment-method/{id}")
    fun getPaymentMethodById(@PathVariable id : Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.getById(id),"Get Payment Method with $id successfully"))
    }

    @Operation(summary = "Create payment method  (admin only)")
    @PostMapping("/payment-method")
    fun createPaymentMethod (@RequestBody req: CreatePaymentMethodRequest) : ResponseEntity<ResponseModel>{
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.create(req), "Create Payment Method Successfully"))
    }

    @Operation(summary = "update payment method (admin only)")
    @PutMapping("/payment-method/{id}")
    fun updatePaymentMethod (@PathVariable id : Long, @RequestBody req : UpdatePaymentMethodRequest) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.update(id, req), "Updated Payment Method Successfully"))
    }

    @Operation(summary = "Change Payment Method Status (admin only)")
    @PatchMapping("/payment-method/{id}")
    fun updatePaymentMethodStatus (@PathVariable id : Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.changeStatus(id),"Updated Status Successfully"))
    }

    @Operation(summary = "Delete payment method with condition")
    @DeleteMapping("/payment-method/{id}")
    fun delete (@PathVariable id :Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(paymentMethodService.delete(id), "Delete payment method Successfully"))
    }

    @Operation(summary = "Get All Transaction (manager only)")
    @GetMapping
    fun getAllTransaction() : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(transactionService.getAll(), "Get All Transaction Successfully"))
    }

    @Operation(summary = "Get Transaction By Id (manager only)")
    @GetMapping("/{id}")
    fun getTranacstionById (@PathVariable id : Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(transactionService.getById(id), "Get Transaction by $id successfully"))
    }



}