package com.ChickenKitchen.app.model.dto.response

data class StepResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val categoryId: Long,
    val categoryName: String,
    val stepNumber: Int,
    val isActive: Boolean,
)

data class StepDetailResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val categoryId: Long,
    val categoryName: String,
    val stepNumber: Int,
    val isActive: Boolean,
)

