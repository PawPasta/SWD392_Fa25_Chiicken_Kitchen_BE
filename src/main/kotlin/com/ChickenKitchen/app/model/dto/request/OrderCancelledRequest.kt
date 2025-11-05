package com.ChickenKitchen.app.model.dto.request



data class OrderCancelledRequest(
    val orderId: Long,
    val reason: String
)