package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enums.UnitType

data class NutrientResponse(
    val id: Long,
    val name: String,
    val baseUnit: UnitType,
)

data class NutrientDetailResponse(
    val id: Long,
    val name: String,
    val baseUnit: UnitType,
)

