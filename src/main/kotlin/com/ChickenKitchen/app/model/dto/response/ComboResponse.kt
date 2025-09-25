package com.ChickenKitchen.app.model.dto.response

import java.math.BigDecimal

data class ComboResponse(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val cal: Int,
    val isActive: Boolean
)

data class ComboDetailResponse(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val cal: Int,
    val isActive: Boolean,
    val items: List<ComboItemResponse>?
)

data class ComboItemResponse(
    val recipeId: Long,
    val recipeName: String,
    val quantity: Int
)

