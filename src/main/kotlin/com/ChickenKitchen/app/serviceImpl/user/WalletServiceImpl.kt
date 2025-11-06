package com.ChickenKitchen.app.serviceImpl.user

import com.ChickenKitchen.app.enums.TransactionStatus
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.handler.WalletNotEnoughBalance
import com.ChickenKitchen.app.handler.WalletNotFoundException
import com.ChickenKitchen.app.mapper.toWalletResponse
import com.ChickenKitchen.app.model.dto.response.UserWalletResponse
import com.ChickenKitchen.app.model.entity.payment.Payment
import com.ChickenKitchen.app.model.entity.payment.Transaction
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.payment.TransactionRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.repository.user.WalletRepository
import com.ChickenKitchen.app.service.user.WalletService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
class WalletServiceImpl (
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val transactionRepository: TransactionRepository,

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

    override fun getMyWallet(): UserWalletResponse {
        val email = SecurityContextHolder.getContext().authentication.name

        val user = userRepository.findByEmail(email)
            ?: throw WalletNotFoundException("User not found with email: $email")

        val wallet = walletRepository.findByUser(user)
            ?: throw WalletNotFoundException("Wallet not found for user: ${user.email}")

        return wallet.toWalletResponse()
    }

    override fun refundToWallet(
        user: User,
        payment: Payment,
        amount: Int
    ) {
        if (amount <= 0) return

        val wallet = walletRepository.findByUser(user)
            ?: throw UserNotFoundException("Wallet not found for user ${user.email}")

        wallet.balance += amount
        walletRepository.save(wallet)

        val method = paymentMethodRepository.findByName("Wallet")
        val txn = Transaction(
            amount = amount,
            payment = payment,
            wallet = wallet,
            transactionType = TransactionStatus.DEBIT,
            paymentMethod = method
        )
        transactionRepository.save(txn)
    }


}