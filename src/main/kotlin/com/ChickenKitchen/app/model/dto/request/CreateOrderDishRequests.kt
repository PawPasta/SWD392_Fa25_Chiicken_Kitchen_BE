package com.ChickenKitchen.app.model.dto.request

data class CreateExistingDishRequest(
    val storeId: Long,
    val dishId: Long,
    val quantity: Int = 1,
)

data class CreateCustomDishRequest(
    val storeId: Long,
    val note: String? = null,
    val selections: List<StepSelection>,
    val isCustom: Boolean = true,
)

