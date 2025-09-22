package com.ChickenKitchen.app.model.dto.response

import java.math.BigDecimal

data class CookingMethodResponse(
    val id: Long,
    val name: String,
    val basePrice: BigDecimal
)

data class CookingMethodDetailResponse(
    val id: Long,
    val name: String,
    val note: String?,
    val basePrice: BigDecimal,
    val effects: List<CookingMethodEffectResponse>
)

data class CookingMethodEffectResponse(
    val nutrientId: Long,
    val nutrientName: String,
    val effectType: String,
    val value: BigDecimal
)