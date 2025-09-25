package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.IngredientCategory
import com.ChickenKitchen.app.enum.UnitEnum
import com.ChickenKitchen.app.model.entity.category.Category
import java.math.BigDecimal

data class IngredientResponse(
    val id: Long,
    val name: String,
    val baseUnit: UnitEnum,
    val baseQuantity: Int,
    val price: BigDecimal,
    val cal: Int,
    val image: String?,
    val category: String,
    val isActive: Boolean
)

data class IngredientDetailResponse(
    val id: Long,
    val name: String,
    val baseUnit: UnitEnum,
    val baseQuantity: Int,
    val price: BigDecimal,
    val cal: Int,
    val image: String?,
    val category: String,
    val isActive: Boolean,
    val nutrients: List<IngredientNutrientResponse>?
)

data class IngredientNutrientResponse(
    val nutrientId: Long,
    val nutrientName: String,
    val amount: BigDecimal,
    val baseUnit: UnitEnum
)
