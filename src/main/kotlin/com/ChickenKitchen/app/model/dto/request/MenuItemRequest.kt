package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.MenuCategory
import java.math.BigDecimal

data class MenuItemNutrientInput(
    val nutrientId: Long,
    val quantity: BigDecimal,
)

data class CreateMenuItemRequest(
    val name: String,
    val category: MenuCategory,
    val isActive: Boolean = true,
    val imageUrl: String? = null,
    val nutrients: List<MenuItemNutrientInput>? = null,
)

// Note: MenuItem entity has immutable name/category; only isActive and imageUrl can change
// If nutrients provided in update, it will replace the current set
data class UpdateMenuItemRequest(
    val isActive: Boolean? = null,
    val imageUrl: String? = null,
    val nutrients: List<MenuItemNutrientInput>? = null,
)
