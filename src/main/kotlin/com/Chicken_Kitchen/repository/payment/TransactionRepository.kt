package com.Chicken_Kitchen.repository.payment

import com.Chicken_Kitchen.model.entity.transaction.Transaction
import com.Chicken_Kitchen.enum.TransactionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findAllByUserId(userId: Long): List<Transaction>
    fun findAllByOrderId(orderId: Long): List<Transaction>
    fun findAllByTransactionType(transactionType: TransactionType): List<Transaction>
    fun findByIdAndUserId(id: Long, userId: Long): Optional<Transaction>
}
