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
)
