package com.ChickenKitchen.app.model.dto.request

data class CreatePaymentMethodRequest (
    val name: String,
    val description: String,
    val isActive : Boolean = false
)

data class UpdatePaymentMethodRequest (
    val name: String?,
    val description: String?,
    val isActive: Boolean,
)