package com.ChickenKitchen.app.model.dto.response

data class CategoryResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val isActive: Boolean
)

data class CategoryDetailResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val isActive: Boolean
)

