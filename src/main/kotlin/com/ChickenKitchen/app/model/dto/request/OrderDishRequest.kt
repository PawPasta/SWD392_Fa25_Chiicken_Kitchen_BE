package com.ChickenKitchen.app.model.dto.request

data class CreateDishRequest(
    val storeId: Long,
    val name: String? = null,
    val isCustomizable: Boolean = true,
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

