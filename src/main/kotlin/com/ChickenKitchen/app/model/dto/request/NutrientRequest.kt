package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.UnitEnum

// Request dùng cho admin để CRUD Nutrient
data class CreateNutrientRequest(
    val name: String,
    val baseUnit: UnitEnum
)

data class UpdateNutrientRequest(
    val name: String? = null,
    val baseUnit: UnitEnum? = null
)
