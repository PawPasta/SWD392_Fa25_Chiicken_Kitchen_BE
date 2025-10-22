package com.ChickenKitchen.app.model.dto.response

import java.sql.Timestamp

data class OrderBriefResponse(
    val orderId: Long,
    val storeId: Long,
    val status: String,
    val totalPrice: Int,
    val createdAt: Timestamp?,
    val pickupTime: Timestamp?
)

