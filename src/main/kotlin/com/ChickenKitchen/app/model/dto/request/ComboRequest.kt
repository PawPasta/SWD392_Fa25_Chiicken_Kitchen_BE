package com.ChickenKitchen.app.model.dto.request

import java.math.BigDecimal

// Request dùng cho admin để CRUD Combo
data class CreateComboRequest(
    val name: String,
    val price: BigDecimal,
    val cal: Int,
    val isActive: Boolean = true,
    val items: List<ComboItemRequest>? = null
)

data class UpdateComboRequest(
    val name: String? = null,
    val price: BigDecimal? = null,
    val cal: Int? = null,
    val isActive: Boolean? = null,
    val items: List<ComboItemRequest>? = null
)

data class ComboItemRequest(
    val recipeId: Long,
    val quantity: Int
)

