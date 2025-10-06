package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.TransactionType
import java.math.BigDecimal


data class TransactionResponse(
    val id: Long,
    val orderId: Long,
    val username: String,
    val paymentMethodName: String,
    val transactionType: TransactionType,
    val amount: BigDecimal,
    val note: String? = null
)

data class TransactionDetailResponse(
    val id: Long,
    val orderId: Long,
    val username: String,
    val paymentMethodName: String,
    val transactionType: String,
    val createAt: String,
    val amount: BigDecimal,
    val note: String? = null
)