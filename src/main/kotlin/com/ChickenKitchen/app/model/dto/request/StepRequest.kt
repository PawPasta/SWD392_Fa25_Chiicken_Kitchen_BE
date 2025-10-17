package com.ChickenKitchen.app.model.dto.request

data class CreateStepRequest(
    val name: String,
    val description: String? = null,
    val categoryId: Long,
    val stepNumber: Int,
    val isActive: Boolean = true,
)

data class UpdateStepRequest(
    val name: String? = null,
    val description: String? = null,
    val categoryId: Long? = null,
    val stepNumber: Int? = null,
    val isActive: Boolean? = null,
)

data class StepOrderRequest(
    val stepNumber: Int,
)

