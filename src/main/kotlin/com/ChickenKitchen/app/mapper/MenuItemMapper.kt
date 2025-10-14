package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.menu.MenuItem
import com.ChickenKitchen.app.model.dto.response.MenuItemResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemDetailResponse

fun MenuItem.toMenuItemResponse(): MenuItemResponse =
    MenuItemResponse(
        id = this.id!!,
        name = this.name,
        category = this.category,
        isActive = this.isActive,
        imageUrl = this.imageUrl,
    )

fun List<MenuItem>.toMenuItemResponseList(): List<MenuItemResponse> =
    this.map { it.toMenuItemResponse() }

fun MenuItem.toMenuItemDetailResponse(): MenuItemDetailResponse =
    MenuItemDetailResponse(
        id = this.id!!,
        name = this.name,
        category = this.category,
        isActive = this.isActive,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.toString(),
    )
