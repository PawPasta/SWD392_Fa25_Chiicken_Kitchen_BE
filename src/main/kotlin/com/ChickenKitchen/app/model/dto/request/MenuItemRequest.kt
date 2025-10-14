package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.MenuCategory

data class CreateMenuItemRequest(
    val name: String,
    val category: MenuCategory,
    val isActive: Boolean = true,
    val imageUrl: String? = null,
)

// Note: MenuItem entity has immutable name/category; only isActive and imageUrl can change
data class UpdateMenuItemRequest(
    val isActive: Boolean? = null,
    val imageUrl: String? = null,
)
