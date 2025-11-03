package com.ChickenKitchen.app.model.dto.response

data class CreatedStepItem(
    val menuItemId: Long,
    val quantity: Int,
)

data class CreatedOrderStep(
    val id: Long,
    val stepId: Long,
    val items: List<CreatedStepItem>
)

data class AddDishResponse(
    val orderId: Long,
    val dishId: Long,
    val status: String,
    val createdSteps: List<CreatedOrderStep> 
)
