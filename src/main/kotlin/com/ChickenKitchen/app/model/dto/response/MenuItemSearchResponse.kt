package com.ChickenKitchen.app.model.dto.response

data class MenuItemSearchResponse(
    val items: List<MenuItemResponse>,
    val total: Long
)

