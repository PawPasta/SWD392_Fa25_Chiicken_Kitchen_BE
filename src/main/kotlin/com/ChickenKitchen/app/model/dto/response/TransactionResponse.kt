package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enums.TransactionStatus
import java.sql.Timestamp


data class PaymentMethodResponse (
    val id: Long,
    val name: String,
    val description: String?,
    val isActive: Boolean
)

data class TransactionResponse (
    val id: Long,
    val userId: Long?,
    val orderId: Long?,
    val paymentMethodId: Long?,
    val transactionStatus: TransactionStatus,
    val amount: Int,
    val createAt: Timestamp?,
    val note: String?,
)