package com.ChickenKitchen.app.service.user

import java.math.BigDecimal

interface WalletService {
    fun getBalance(): BigDecimal
    fun addFunds(amount: Double)
    fun deductFunds(amount: Double)
}