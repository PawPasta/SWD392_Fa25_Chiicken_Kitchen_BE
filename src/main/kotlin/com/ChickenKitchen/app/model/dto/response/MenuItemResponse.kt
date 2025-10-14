package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.MenuCategory
import java.math.BigDecimal

data class MenuItemResponse(
    val id: Long,
    val name: String,
    val category: MenuCategory,
    val isActive: Boolean,
    val imageUrl: String?,
)

data class MenuItemNutrientBriefResponse(
    val id: Long, // nutrient id
    val name: String,
    val quantity: BigDecimal,
)

data class MenuItemDetailResponse(
    val id: Long,
    val name: String,
    val category: MenuCategory,
    val isActive: Boolean,
    val imageUrl: String?,
    val createdAt: String,
    val nutrients: List<MenuItemNutrientBriefResponse> = emptyList(),
)
