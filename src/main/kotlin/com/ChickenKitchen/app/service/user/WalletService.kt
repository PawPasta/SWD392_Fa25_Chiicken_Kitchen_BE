package com.ChickenKitchen.app.service.user

import com.ChickenKitchen.app.model.dto.response.UserWalletResponse
import com.ChickenKitchen.app.model.entity.payment.Payment
import com.ChickenKitchen.app.model.entity.user.User


interface WalletService {

    fun deductFromWallet(user : User, amount: Int)

    fun getMyWallet() : UserWalletResponse

    fun refundToWallet(user: User, payment: Payment, amount: Int)
}