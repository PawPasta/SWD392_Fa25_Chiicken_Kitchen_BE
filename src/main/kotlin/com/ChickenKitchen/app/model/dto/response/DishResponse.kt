package com.ChickenKitchen.app.model.dto.response

data class DishResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val cal: Int,
    val isCustom: Boolean,
    val note: String?,
    val imageUrl: String?,
    val nutrients: List<MenuItemNutrientBriefResponse> = emptyList()
)

data class DishDetailResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val cal: Int,
    val isCustom: Boolean,
    val note: String?,
    val imageUrl: String?,
    val createdAt: String,
    val updatedAt: String?,
    val steps: List<DishStepResponse> = emptyList(),
    val nutrients: List<MenuItemNutrientBriefResponse> = emptyList()
)

data class DishStepItemResponse(
    val menuItemId: Long,
    val name: String,
    val imageUrl: String?,
    val quantity: Int,
    val price: Int,
    val cal: Int
)

data class DishStepResponse(
    val stepId: Long,
    val stepName: String,
    val items: List<DishStepItemResponse>
)
