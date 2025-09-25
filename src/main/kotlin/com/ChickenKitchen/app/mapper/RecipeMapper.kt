package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.recipe.Recipe
import com.ChickenKitchen.app.model.dto.response.RecipeResponse
import com.ChickenKitchen.app.model.dto.response.RecipeDetailResponse
import com.ChickenKitchen.app.model.dto.response.RecipeIngredientResponse

fun Recipe.toRecipeResponse() = RecipeResponse(
    id = this.id!!,
    name = this.name,
    isCustomizable = this.isCustomizable,
    price = this.price,
    cal = this.cal,
    image = this.image,
    category = this.category,
    isActive = this.isActive
)

fun Recipe.toRecipeDetailResponse() = RecipeDetailResponse(
    id = this.id!!,
    name = this.name,
    description = this.description,
    isCustomizable = this.isCustomizable,
    price = this.price,
    cal = this.cal,
    ingredientSnapshot = this.ingredientSnapshot,
    image = this.image,
    category = this.category,
    isActive = this.isActive, 
    ingredient = this.recipe_ingredients.map {
        RecipeIngredientResponse(
            ingredientId = it.ingredient.id!!,
            ingredientName = it.ingredient.name,
            quantity = it.quantity,
            baseUnit = it.baseUnit,
            price = it.price,
            cal = it.cal
        )
    }
)

fun List<Recipe>.toRecipeResponseList() = this.map { it.toRecipeResponse() }

