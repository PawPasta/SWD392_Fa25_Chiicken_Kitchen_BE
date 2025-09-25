package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.IngredientCategory
import com.ChickenKitchen.app.enum.UnitEnum
import java.math.BigDecimal

// Request dùng cho admin để CRUD Ingredient
data class CreateIngredientRequest(
    val name: String,
    val baseUnit: UnitEnum,
    val baseQuantity: Int = 100,
    val basePrice: BigDecimal,
    val baseCal: Int,
    val image: String? = null,
    val category: String? = null,
    val isActive: Boolean = true,
    val nutrients: List<IngredientNutrientRequest>? = null
)

data class UpdateIngredientRequest(
    val name: String? = null,
    val baseUnit: UnitEnum? = null,
    val baseQuantity: Int? = null,
    val basePrice: BigDecimal? = null,
    val baseCal: Int? = null,
    val image: String? = null,
    val category: String? = null,
    val isActive: Boolean? = null,
    val nutrients: List<IngredientNutrientRequest>? = null
)

data class IngredientNutrientRequest(
    val nutrientId: Long,
    val amount: BigDecimal
)
