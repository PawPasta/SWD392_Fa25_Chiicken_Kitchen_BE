package com.ChickenKitchen.app.serviceImpl.payment

import com.ChickenKitchen.app.enums.TransactionStatus
import com.ChickenKitchen.app.handler.TransactionCannotCreateException
import com.ChickenKitchen.app.handler.TransactionNotFoundException
import com.ChickenKitchen.app.handler.WalletNotFoundException
import com.ChickenKitchen.app.mapper.toListTransactionResponse
import com.ChickenKitchen.app.mapper.toTransactionResponse
import com.ChickenKitchen.app.model.dto.response.TransactionResponse
import com.ChickenKitchen.app.model.entity.payment.Payment
import com.ChickenKitchen.app.repository.payment.TransactionRepository
import com.ChickenKitchen.app.repository.user.WalletRepository
import com.ChickenKitchen.app.service.payment.TransactionService
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.model.entity.payment.Transaction
import org.springframework.stereotype.Service


@Service
class TransactionServiceImpl (
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository
): TransactionService {

    override fun getAll(): List<TransactionResponse>? {
        val list = transactionRepository.findAll()
        if (list.isEmpty()) return null
        return list.toListTransactionResponse()
    }

    override fun getById(id: Long): TransactionResponse {
        val transaction = transactionRepository.findById(id)
            .orElseThrow { TransactionNotFoundException("Cannot find Transaction with id $id") }
        return transaction.toTransactionResponse()
    }

    override fun createPaymentTransaction(
        payment: Payment,
        order: Order,
        paymentMethod: PaymentMethod
    ): String {
        val wallet = walletRepository.findByUser(order.user)
            ?: throw WalletNotFoundException("Wallet not found for user ${order.user.id}")

        return when (paymentMethod.name.uppercase()) {

            "VNPAY", "MOMO", "ZALOPAY" -> {
                val depositTransaction = Transaction(
                    payment = payment,
                    wallet = wallet,
                    paymentMethod = paymentMethod,
                    transactionType = TransactionStatus.DEBIT,
                    amount = payment.finalAmount,
                    note = "Deposit via ${paymentMethod.name.uppercase()}"
                )
                transactionRepository.save(depositTransaction)

                val paymentTransaction = Transaction(
                    payment = payment,
                    wallet = wallet,
                    paymentMethod = paymentMethod,
                    transactionType = TransactionStatus.CREDIT,
                    amount = payment.finalAmount,
                    note = "Payment for order ${order.id} via ${paymentMethod.name.uppercase()}"
                )
                transactionRepository.save(paymentTransaction)

                "Payment via ${paymentMethod.name.uppercase()} successful"
            }

            "WALLET" -> {
                val paymentTransaction = Transaction(
                    payment = payment,
                    wallet = wallet,
                    paymentMethod = paymentMethod,
                    transactionType = TransactionStatus.CREDIT,
                    amount = payment.finalAmount,
                    note = "Payment for order ${order.id} via wallet"
                )
                transactionRepository.save(paymentTransaction)

                "Payment via wallet successful"
            }

            else -> throw TransactionCannotCreateException(
                "Payment method ${paymentMethod.name} is not supported for transactions"
            )
        }
    }

}
