package com.ChickenKitchen.app.serviceImpl.user

import com.ChickenKitchen.app.service.user.WalletService
import com.ChickenKitchen.app.repository.user.WalletRepository
import com.ChickenKitchen.app.handler.WalletNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.context.SecurityContextHolder
import java.math.BigDecimal

@Service
class WalletServiceImpl(
    private val walletRepository: WalletRepository,
) : WalletService {

    override fun getBalance(): BigDecimal {
        val username = SecurityContextHolder.getContext().authentication.name
        val wallet = walletRepository.findByUserUsername(username)
            ?: throw WalletNotFoundException("Wallet not found for user")
        return wallet.balance
    }

    override fun addFunds(amount: Double) {
        // Implementation here
    }

    override fun deductFunds(amount: Double) {
        // Implementation here
    }
}