package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enums.UnitType

data class CreateNutrientRequest(
    val name: String,
    val baseUnit: UnitType,
)

data class UpdateNutrientRequest(
    val name: String? = null,
    val baseUnit: UnitType? = null,
)

