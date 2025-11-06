package com.ChickenKitchen.app.model.dto.request

data class CreateDishBaseRequest(
    val name: String,
    val price: Int,
    val cal: Int,
    val note: String? = null,
    val isCustom: Boolean = false,
    val imageUrl: String? = null,
)

data class UpdateDishBaseRequest(
    val name: String? = null,
    val price: Int? = null,
    val cal: Int? = null,
    val note: String? = null,
    val isCustom: Boolean? = null,
    val imageUrl: String? = null,
)
