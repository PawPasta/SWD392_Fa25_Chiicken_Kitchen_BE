package com.ChickenKitchen.app.model.dto.response


data class PaymentMethodResponse (
    val id: Long,
    val name: String,
    val description: String?,
    val isActive: Boolean
)
