package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.cooking.CookingMethod
import com.ChickenKitchen.app.model.entity.cooking.CookingEffect
import com.ChickenKitchen.app.model.dto.response.CookingMethodResponse
import com.ChickenKitchen.app.model.dto.response.CookingMethodDetailResponse
import com.ChickenKitchen.app.model.dto.response.CookingMethodEffectResponse

fun CookingMethod.toCookingMethodResponse() = CookingMethodResponse(
    id = this.id!!,
    name = this.name,
    basePrice = this.basePrice
)

fun CookingMethod.toCookingMethodDetailResponse() = CookingMethodDetailResponse(
    id = this.id!!,
    name = this.name,
    note = this.note,
    basePrice = this.basePrice,
    effects = this.cooking_effects.map { it.toCookingMethodEffectResponse() }
)

fun CookingEffect.toCookingMethodEffectResponse() = CookingMethodEffectResponse(
    nutrientId = this.nutrient.id!!,
    nutrientName = this.nutrient.name,
    effectType = this.effectType.name,
    value = this.value
)

fun List<CookingMethod>.toCookingMethodResponseList() = this.map { it.toCookingMethodResponse() }
