package com.ChickenKitchen.app.model.dto.response

import java.sql.Timestamp

data class CurrentStepItemResponse(
    val menuItemId: Long,
    val menuItemName: String,
    val imageUrl: String?,
    val quantity: Int,
    val price: Int,
    val cal: Int
)

data class CurrentStepResponse(
    val stepId: Long,
    val stepName: String,
    val items: List<CurrentStepItemResponse>
)

data class CurrentDishResponse(
    val dishId: Long,
    val name: String,
    val isCustom: Boolean,
    val note: String?,
    val price: Int,
    val cal: Int,
    val updatedAt: Timestamp?,
    val steps: List<CurrentStepResponse>
)

data class OrderCurrentResponse(
    val orderId: Long,
    val status: String,
    val cleared: Boolean,
    val dishes: List<CurrentDishResponse>
)
