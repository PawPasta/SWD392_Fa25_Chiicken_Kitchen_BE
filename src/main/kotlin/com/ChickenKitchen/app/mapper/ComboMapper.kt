package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.ComboDetailResponse
import com.ChickenKitchen.app.model.dto.response.ComboItemResponse
import com.ChickenKitchen.app.model.dto.response.ComboResponse
import com.ChickenKitchen.app.model.entity.combo.Combo

fun Combo.toComboResponse() = ComboResponse(
    id = this.id!!,
    name = this.name,
    price = this.price,
    cal = this.cal,
    isActive = this.isActive
)

fun Combo.toComboDetailResponse() = ComboDetailResponse(
    id = this.id!!,
    name = this.name,
    price = this.price,
    cal = this.cal,
    isActive = this.isActive,
    items = this.combo_items.map { item ->
        ComboItemResponse(
            recipeId = item.recipe.id!!,
            recipeName = item.recipe.name,
            quantity = item.quantity
        )
    }
)

fun List<Combo>.toComboResponseList() = this.map { it.toComboResponse() }

