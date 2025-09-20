package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.IngredientCategory
import com.ChickenKitchen.app.enum.UnitEnum
import java.math.BigDecimal

// Request dùng cho admin để CRUD Ingredient
data class CreateIngredientRequest(
    val name: String,
    val baseUnit: UnitEnum,
    val baseQuantity: Int,
    val basePrice: BigDecimal,
    val image: String? = null,
    val category: IngredientCategory? = null,
    val isActive: Boolean = true
)

data class UpdateIngredientRequest(
    val name: String? = null,
    val baseUnit: UnitEnum? = null,
    val baseQuantity: Int? = null,
    val basePrice: BigDecimal? = null,
    val image: String? = null,
    val category: IngredientCategory? = null,
    val isActive: Boolean? = null
)
