package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.EffectType

data class CookingEffectResponse(
    val id: Long,
    val methodId: Long,
    val nutrientId: Long,
    val effectType: EffectType,
    val value: Int
)

data class CookingEffectDetailResponse(
    val id: Long,
    val methodId: Long,
    val nutrientId: Long,
    val effectType: EffectType,
    val value: Int,
    val description: String?,
)
