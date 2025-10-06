package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.payment.TransactionService

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/api/transactions")
class TransactionController (
    private val transactionService: TransactionService
){

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "get All Transaction (only admin)")
    @GetMapping("/transactions/getAll")
    fun getAllTransaction(): ResponseEntity<ResponseModel> {
        val transactions = transactionService.getAll()
        return ResponseEntity.ok(ResponseModel.success(transactions, "Get all transactions successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "get Transaction by ID (only admin)")
    @GetMapping("/transactions/{id}")
    fun getTransactionById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val transaction = transactionService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(transaction, "Get transaction successfully!"))
    }
}