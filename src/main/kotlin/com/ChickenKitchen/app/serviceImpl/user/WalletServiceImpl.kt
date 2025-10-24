package com.ChickenKitchen.app.serviceImpl.user

import com.ChickenKitchen.app.handler.WalletNotEnoughBalance
import com.ChickenKitchen.app.handler.WalletNotFoundException
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.repository.user.WalletRepository
import com.ChickenKitchen.app.service.user.WalletService
import org.springframework.stereotype.Service


@Service
class WalletServiceImpl (
    private val walletRepository: WalletRepository,

): WalletService{

    override fun deductFromWallet(user: User, amount: Int) {
        val wallet = walletRepository.findByUser(user)
            ?: throw WalletNotFoundException("Wallet not found for user ${user.id}")

        if (wallet.balance < amount) {
            throw WalletNotEnoughBalance("Insufficient wallet balance for this payment")
        }

        wallet.balance -= amount
        walletRepository.save(wallet)
    }
}