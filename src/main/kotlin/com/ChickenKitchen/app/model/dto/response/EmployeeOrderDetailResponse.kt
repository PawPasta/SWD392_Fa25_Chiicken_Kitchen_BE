package com.ChickenKitchen.app.model.dto.response

import java.sql.Timestamp

data class StepItemWithIngredientsResponse(
    val menuItemId: Long,
    val menuItemName: String,
    val imageUrl: String?,
    val quantity: Int,
    val price: Int,
    val cal: Int,
    val ingredients: List<IngredientResponse>,
)

data class StepWithIngredientsResponse(
    val stepId: Long,
    val stepName: String,
    val items: List<StepItemWithIngredientsResponse>
)

data class DishWithIngredientsResponse(
    val dishId: Long,
    val name: String,
    val isCustom: Boolean,
    val note: String?,
    val price: Int,
    val cal: Int,
    val updatedAt: Timestamp?,
    val steps: List<StepWithIngredientsResponse>
)

data class EmployeeOrderDetailResponse(
    val orderId: Long,
    val status: String,
    val totalPrice: Int,
    val createdAt: Timestamp?,
    val pickupTime: Timestamp?,
    val customer: OrderCustomerResponse,
    val dishes: List<DishWithIngredientsResponse>
)

