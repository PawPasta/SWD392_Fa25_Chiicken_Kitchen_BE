package com.ChickenKitchen.app.model.dto.response

import java.sql.Timestamp

data class FeedbackResponse(
    val id: Long,
    val orderId: Long,
    val storeId: Long,
    val rating: Int,
    val message: String?,
    val reply: String?,
    val createdAt: Timestamp?,
    val updatedAt: Timestamp?
)

