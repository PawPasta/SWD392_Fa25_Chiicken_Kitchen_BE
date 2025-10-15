package com.ChickenKitchen.app.model.dto.response

data class CreatedOrderStep(
    val id: Long,
    val stepId: Long,
    val menuItemId: Long,
    val quantity: Int,
)

data class AddDishResponse(
    val orderId: Long,
    val dishId: Long,
    val status: String,
    val createdSteps: List<CreatedOrderStep> 
)

