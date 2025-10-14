package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.MenuCategory

data class MenuItemResponse(
    val id: Long,
    val name: String,
    val category: MenuCategory,
    val isActive: Boolean,
    val imageUrl: String?,
)

data class MenuItemDetailResponse(
    val id: Long,
    val name: String,
    val category: MenuCategory,
    val isActive: Boolean,
    val imageUrl: String?,
    val createdAt: String,
)
