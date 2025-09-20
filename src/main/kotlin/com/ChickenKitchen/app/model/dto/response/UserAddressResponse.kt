package com.ChickenKitchen.app.model.dto.response

data class UserAddressResponse(
    val id: Long,
    val recipientName: String,
    val phone: String,
    val addressLine: String,
    val city: String,
    val isDefault: Boolean
)