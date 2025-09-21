package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.IngredientCategory
import com.ChickenKitchen.app.enum.UnitEnum
import java.math.BigDecimal

data class IngredientResponse(
    val id: Long,
    val name: String,
    val baseUnit: UnitEnum,
    val baseQuantity: Int,
    val basePrice: BigDecimal,
    val image: String?,
    val category: IngredientCategory?,
    val isActive: Boolean
)

data class IngredientDetailResponse(
    val id: Long,
    val name: String,
    val baseUnit: UnitEnum,
    val baseQuantity: Int,
    val basePrice: BigDecimal,
    val image: String?,
    val category: IngredientCategory?,
    val isActive: Boolean,
    val nutrients: List<IngredientNutrientResponse>?
)

data class IngredientNutrientResponse(
    val nutrientId: Long,
    val nutrientName: String,
    val amount: BigDecimal,
    val baseUnit: UnitEnum
)