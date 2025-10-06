package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.TransactionType
import java.math.BigDecimal

data class CreateTransactionRequest(
    val orderId: Long,
    val userId: Long,
    val paymentMethodId: Long,
    val transactionType: TransactionType,
    val amount: BigDecimal,
    val note: String? = null
)