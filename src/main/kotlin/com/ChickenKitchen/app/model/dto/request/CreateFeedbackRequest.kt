package com.ChickenKitchen.app.model.dto.request

data class CreateFeedbackRequest(
    val rating: Int,
    val message: String? = null
)

