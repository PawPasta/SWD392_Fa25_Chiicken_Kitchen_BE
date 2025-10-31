package com.ChickenKitchen.app.model.dto.response

data class OrderTrackingResponse(
    val orderId: Long,
    val status: String,
    val progress: Int,
    val dishes: List<CurrentDishResponse>
)

