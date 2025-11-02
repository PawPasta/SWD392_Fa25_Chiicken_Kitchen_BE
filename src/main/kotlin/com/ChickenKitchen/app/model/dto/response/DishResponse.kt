package com.ChickenKitchen.app.model.dto.response

data class DishResponse(
    val id: Long,
    val price: Int,
    val cal: Int,
    val isCustom: Boolean,
    val note: String?,
)

data class DishDetailResponse(
    val id: Long,
    val price: Int,
    val cal: Int,
    val isCustom: Boolean,
    val note: String?,
    val createdAt: String,
    val updatedAt: String?,
)

