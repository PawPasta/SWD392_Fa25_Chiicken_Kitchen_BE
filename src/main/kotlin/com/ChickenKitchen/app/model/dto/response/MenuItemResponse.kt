package com.ChickenKitchen.app.model.dto.response
import java.math.BigDecimal

data class MenuItemResponse(
    val id: Long,
    val name: String,
    val categoryId: Long,
    val categoryName: String,
    val isActive: Boolean,
    val imageUrl: String?,
    val price: Int,
)

data class MenuItemNutrientBriefResponse(
    val id: Long, // nutrient id
    val name: String,
    val quantity: BigDecimal,
)

data class MenuItemDetailResponse(
    val id: Long,
    val name: String,
    val categoryId: Long,
    val categoryName: String,
    val isActive: Boolean,
    val imageUrl: String?,
    val createdAt: String,
    val price: Int,
    val nutrients: List<MenuItemNutrientBriefResponse> = emptyList(),
)
