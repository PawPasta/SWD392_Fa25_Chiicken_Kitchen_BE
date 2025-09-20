package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.ingredient.Nutrient
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.model.dto.response.*

/* ===== Nutrient Mapping ===== */
fun Nutrient.toNutrientResponse() = NutrientResponse(
    id = this.id!!,
    name = this.name,
    baseUnit = this.baseUnit
)

fun Nutrient.toNutrientDetailResponse() = NutrientDetailResponse(
    id = this.id!!,
    name = this.name,
    baseUnit = this.baseUnit,
    createdAt = this.toString(), // TODO: thay bằng createdAt thực tế nếu có
    updatedAt = this.toString()
)

fun List<Nutrient>.toNutrientResponseList() = this.map { it.toNutrientResponse() }