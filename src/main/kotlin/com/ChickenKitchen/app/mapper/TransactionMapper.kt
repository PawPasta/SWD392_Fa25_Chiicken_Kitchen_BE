package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.TransactionDetailResponse
import com.ChickenKitchen.app.model.dto.response.TransactionResponse
import com.ChickenKitchen.app.model.entity.transaction.Transaction

fun Transaction.toResponseTransaction(): TransactionResponse {
    return TransactionResponse(
        id = this.id!!,  // Safe to use !! if id is guaranteed to be non-null after persistence
        orderId = this.order.id!!,
        username = this.user.username,
        paymentMethodName = this.paymentMethod.name.name,
        transactionType = this.transactionType,
        amount = this.amount,
        note = this.note
    )
}


fun Transaction.toDetailedResponseTransaction(): TransactionDetailResponse {
    return TransactionDetailResponse(
        id = this.id!!,
        orderId = this.order.id!!,
        username = this.user.username,
        paymentMethodName = this.paymentMethod.name.name,
        transactionType = this.transactionType.name,
        amount = this.amount,
        createAt = this.createdAt.toString(),
        note = this.note
    )
}

fun List<Transaction>.toResponseTransactionList(): List<TransactionResponse> {
    return this.map { it.toResponseTransaction() }
}