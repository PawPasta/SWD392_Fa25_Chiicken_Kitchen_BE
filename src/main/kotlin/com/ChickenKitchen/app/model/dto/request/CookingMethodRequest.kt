package com.ChickenKitchen.app.model.dto.request

import java.math.BigDecimal

// Request cho CRUD CookingMethod
data class CreateCookingMethodRequest(
    val name: String,
    val note: String? = null,
    val basePrice: BigDecimal = BigDecimal.ZERO
)

data class UpdateCookingMethodRequest(
    val name: String? = null,
    val note: String? = null,
    val basePrice: BigDecimal? = null
)
