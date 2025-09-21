package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.model.dto.response.IngredientResponse
import com.ChickenKitchen.app.model.dto.response.IngredientDetailResponse
import com.ChickenKitchen.app.model.dto.response.IngredientNutrientResponse

fun Ingredient.toIngredientResponse() = IngredientResponse(
    id = this.id!!,
    name = this.name,
    baseUnit = this.baseUnit,
    baseQuantity = this.baseQuantity,
    basePrice = this.basePrice,
    image = this.image,
    category = this.category,
    isActive = this.isActive
)

fun Ingredient.toIngredientDetailResponse() = IngredientDetailResponse(
    id = this.id!!,
    name = this.name,
    baseUnit = this.baseUnit,
    baseQuantity = this.baseQuantity,
    basePrice = this.basePrice,
    image = this.image,
    category = this.category, 
    isActive = this.isActive,
    nutrients = this.ingredient_nutrients.map {
        IngredientNutrientResponse(
            nutrientId = it.nutrient.id!!,
            nutrientName = it.nutrient.name,
            amount = it.amount,
            baseUnit = it.nutrient.baseUnit
        )
    }
)

fun List<Ingredient>.toIngredientResponseList() = this.map { it.toIngredientResponse() }