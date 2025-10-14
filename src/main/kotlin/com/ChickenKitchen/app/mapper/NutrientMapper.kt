package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.menu.Nutrient
import com.ChickenKitchen.app.model.dto.response.NutrientResponse
import com.ChickenKitchen.app.model.dto.response.NutrientDetailResponse

fun Nutrient.toNutrientResponse(): NutrientResponse =
    NutrientResponse(
        id = this.id!!,
        name = this.name,
        baseUnit = this.baseUnit,
    )

fun List<Nutrient>.toNutrientResponseList(): List<NutrientResponse> =
    this.map { it.toNutrientResponse() }

fun Nutrient.toNutrientDetailResponse(): NutrientDetailResponse =
    NutrientDetailResponse(
        id = this.id!!,
        name = this.name,
        baseUnit = this.baseUnit,
    )

