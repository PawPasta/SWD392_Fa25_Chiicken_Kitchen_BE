package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.EffectType

// Request cho CRUD CookingEffect
data class CreateCookingEffectRequest(
    val methodId: Long,
    val nutrientId: Long,
    val effectType: EffectType,
    val value: Int,
    val description: String? = null
)

data class UpdateCookingEffectRequest(
    val methodId: Long? = null,
    val nutrientId: Long? = null,
    val effectType: EffectType? = null,
    val value: Int? = null,
    val description: String? = null
)
