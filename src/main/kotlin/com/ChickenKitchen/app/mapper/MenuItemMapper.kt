package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.menu.MenuItem
import com.ChickenKitchen.app.model.entity.menu.MenuItemNutrient
import com.ChickenKitchen.app.model.dto.response.MenuItemResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemDetailResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemNutrientBriefResponse
import com.ChickenKitchen.app.model.dto.response.RecipeBriefResponse
import com.ChickenKitchen.app.model.entity.ingredient.Recipe

fun MenuItem.toMenuItemResponse(): MenuItemResponse =
    MenuItemResponse(
        id = this.id!!,
        name = this.name,
        categoryId = this.category.id!!,
        categoryName = this.category.name,
        isActive = this.isActive,
        imageUrl = this.imageUrl,
        price = this.price,
        cal = this.cal,
        description = this.description
    )

fun List<MenuItem>.toMenuItemResponseList(): List<MenuItemResponse> =
    this.map { it.toMenuItemResponse() }

fun MenuItem.toMenuItemDetailResponse(nutrients: List<MenuItemNutrientBriefResponse> = emptyList(), recipes : List<RecipeBriefResponse>): MenuItemDetailResponse =
    MenuItemDetailResponse(
        id = this.id!!,
        name = this.name,
        categoryId = this.category.id!!,
        categoryName = this.category.name,
        isActive = this.isActive,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.toString(),
        nutrients = nutrients,
        recipe = recipes,
        price = this.price,
        cal = this.cal,
        description = this.description
    )

fun List<MenuItemNutrient>.toBriefResponses(): List<MenuItemNutrientBriefResponse> =
    this.map {
        MenuItemNutrientBriefResponse(
            id = it.nutrient.id!!,
            name = it.nutrient.name,
            quantity = it.quantity,
            baseUnit = it.nutrient.baseUnit,
        )
    }

fun List<Recipe>.toRecipeBriefResponse() : List<RecipeBriefResponse> =
    this.map {
        RecipeBriefResponse(
            id = it.ingredient.id!!,
            name = it.ingredient.name
        )
    }
