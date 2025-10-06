package com.ChickenKitchen.app.serviceImpl.payment

import com.ChickenKitchen.app.handler.TransactionNotFoundException
import com.ChickenKitchen.app.mapper.toDetailedResponseTransaction
import com.ChickenKitchen.app.mapper.toResponseTransactionList
import com.ChickenKitchen.app.model.dto.response.TransactionDetailResponse
import com.ChickenKitchen.app.model.dto.response.TransactionResponse
import com.ChickenKitchen.app.repository.payment.TransactionRepository
import com.ChickenKitchen.app.service.payment.TransactionService
import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl (
    private val transactionRepository: TransactionRepository,
): TransactionService {

    override fun getAll(): List<TransactionResponse>? {
        val list = transactionRepository.findAll()
        if (list.isEmpty()) return null
        return list.toResponseTransactionList()
    }

    override fun getById(id: Long): TransactionDetailResponse {
        val transaction = transactionRepository.findById(id).orElse(null)
            ?: throw TransactionNotFoundException("Transaction with id $id not found")
        return transaction.toDetailedResponseTransaction()
    }


}