package com.ChickenKitchen.app.model.dto.request

data class CreateCategoryRequest(
    val name: String,
    val description: String? = null,
)

data class UpdateCategoryRequest(
    val name: String? = null,
    val description: String? = null,
)

