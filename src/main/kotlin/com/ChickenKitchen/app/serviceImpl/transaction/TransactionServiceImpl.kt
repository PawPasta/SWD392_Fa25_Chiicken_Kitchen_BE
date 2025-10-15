package com.ChickenKitchen.app.serviceImpl.transaction

import com.ChickenKitchen.app.mapper.toListTransactionResponse
import com.ChickenKitchen.app.mapper.toTransactionResponse
import com.ChickenKitchen.app.model.dto.response.TransactionResponse
import com.ChickenKitchen.app.repository.payment.TransactionRepository
import com.ChickenKitchen.app.service.transaction.TransactionService
import org.springframework.stereotype.Service


@Service
class TransactionServiceImpl (
    private val transactionRepository: TransactionRepository
): TransactionService {
    override fun getAll(): List<TransactionResponse>? {
        val list = transactionRepository.findAll()
        if (list.isEmpty()) return null
        return list.toListTransactionResponse()
    }

    override fun getById(id: Long): TransactionResponse {
        val transaction = transactionRepository.findById(id).orElseThrow { NoSuchElementException("Cannot find transaction with $id") }
        return transaction.toTransactionResponse()
    }
}