package com.ChickenKitchen.app.model.dto.request

data class CreateUserAddressRequest(
    val recipientName: String,
    val phone: String,
    val addressLine: String,
    val city: String,
    val isDefault: Boolean = false
)
 
data class UpdateUserAddressRequest(
    val recipientName: String?,
    val phone: String?,
    val addressLine: String?,
    val city: String?,
    val isDefault: Boolean?
)