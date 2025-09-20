package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.service.user.WalletService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.http.ResponseEntity
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import io.swagger.v3.oas.annotations.Operation

@RestController
@RequestMapping("/api/wallet")
class WalletController (private val walletService: WalletService) {

    @Operation(summary = "Get wallet balance")
    @GetMapping("/balance")
    fun getBalance(): ResponseEntity<ResponseModel> {
        val balance = walletService.getBalance()
        return ResponseEntity.ok(ResponseModel.success(balance, "Get wallet balance successfully!"))
    }
}