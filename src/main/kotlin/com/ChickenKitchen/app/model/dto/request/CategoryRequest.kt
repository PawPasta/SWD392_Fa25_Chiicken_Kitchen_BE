package com.ChickenKitchen.app.model.dto.request

// Request dùng cho admin để CRUD Category
data class CreateCategoryRequest(
    val name: String,
    val description: String? = null,
    val isActive: Boolean = true
)

data class UpdateCategoryRequest(
    val name: String? = null,
    val description: String? = null,
    val isActive: Boolean? = null
)

