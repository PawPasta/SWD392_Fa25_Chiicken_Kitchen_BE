package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.UnitEnum

data class NutrientResponse(
    val id: Long,
    val name: String,
    val baseUnit: UnitEnum
)

data class NutrientDetailResponse(
    val id: Long,
    val name: String,
    val baseUnit: UnitEnum,
)
