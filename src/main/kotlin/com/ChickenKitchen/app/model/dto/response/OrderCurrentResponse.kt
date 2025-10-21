package com.ChickenKitchen.app.model.dto.response

data class OrderCurrentResponse(
    val orderId: Long,
    val status: String,
    val totalItems: Int,
    val keptItems: Int,
    val cleared: Boolean
)

