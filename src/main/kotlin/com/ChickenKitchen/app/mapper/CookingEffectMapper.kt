package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.cooking.CookingEffect
import com.ChickenKitchen.app.model.dto.response.CookingEffectResponse
import com.ChickenKitchen.app.model.dto.response.CookingEffectDetailResponse

fun CookingEffect.toCookingEffectResponse() = CookingEffectResponse(
    id = this.id!!,
    methodId = this.method.id!!,
    nutrientId = this.nutrientId,
    effectType = this.effectType,
    value = this.value
)

fun CookingEffect.toCookingEffectDetailResponse() = CookingEffectDetailResponse(
    id = this.id!!,
    methodId = this.method.id!!,
    nutrientId = this.nutrientId,
    effectType = this.effectType,
    value = this.value,
    description = this.description,
)

fun List<CookingEffect>.toCookingEffectResponseList() = this.map { it.toCookingEffectResponse() }
