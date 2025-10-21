package com.ChickenKitchen.app.model.dto.request

data class CreateDishRequest(
    val storeId: Long,
    val note: String? = null,
    val selections: List<StepSelection>
)

data class StepSelection(
    val stepId: Long,
    val items: List<SelectionItem>
)

data class SelectionItem(
    val menuItemId: Long,
    val quantity: Int
)
