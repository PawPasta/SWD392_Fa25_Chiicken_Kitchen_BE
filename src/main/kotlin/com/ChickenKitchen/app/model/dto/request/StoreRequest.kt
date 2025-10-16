package com.ChickenKitchen.app.model.dto.request


data class CreateStoreRequest (
    val name : String,
    val address: String,
    val phone: String,
    val createAt: String,
    val isActive : Boolean = false,
)

data class UpdateStoreRequest  (
    val name: String?,
    val address: String?,
    val phone: String?,
    val isActive: Boolean
)