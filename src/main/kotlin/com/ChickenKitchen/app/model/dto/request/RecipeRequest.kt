package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.RecipeCategory
import com.ChickenKitchen.app.enum.UnitEnum
import java.math.BigDecimal

data class CreateRecipeRequest(
    val name: String,
    val description: String? = null,
    val isCustomizable: Boolean = false, // Admin tạo mặc định là món có sẵn
    val basePrice: BigDecimal = BigDecimal.ZERO,
    val baseCal: Int = 0,
    val ingredientSnapshot: String? = "{}",
    val image: String? = null,
    val category: RecipeCategory? = null,
    val isActive: Boolean = true,
    val items: List<RecipeIngredientRequest>? = null,
)

data class UpdateRecipeRequest(
    val name: String? = null,
    val description: String? = null,
    val isCustomizable: Boolean? = null,
    val basePrice: BigDecimal? = null,
    val baseCal: Int? = null,
    val ingredientSnapshot: String? = null,
    val image: String? = null,
    val category: RecipeCategory? = null,
    val isActive: Boolean? = null,
    val items: List<RecipeIngredientRequest>? = null,
)

data class RecipeIngredientRequest(
    val ingredientId: Long,
    val cookingMethodId: Long,
    val quantity: Int,
    val baseUnit: UnitEnum,
)