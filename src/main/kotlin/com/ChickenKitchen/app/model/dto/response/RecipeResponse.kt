package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.RecipeCategory
import com.ChickenKitchen.app.enum.UnitEnum
import java.math.BigDecimal

data class RecipeResponse(
    val id: Long,
    val name: String,
    val isCustomizable: Boolean,
    val basePrice: BigDecimal,
    val baseCal: Int,
    val image: String?,
    val category: RecipeCategory?,
    val isActive: Boolean
)

data class RecipeDetailResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val isCustomizable: Boolean,
    val basePrice: BigDecimal,
    val baseCal: Int,
    val ingredientSnapshot: String?,
    val image: String?,
    val category: RecipeCategory?,
    val isActive: Boolean,
    val ingredient: List<RecipeIngredientResponse>? = null
)

data class RecipeIngredientResponse(
    val ingredientId: Long,
    val ingredientName: String,
    val cookingMethodId: Long,
    val cookingMethodName: String,
    val quantity: Int,
    val baseUnit: UnitEnum,
    val price: BigDecimal,
    val cal: Int
)

