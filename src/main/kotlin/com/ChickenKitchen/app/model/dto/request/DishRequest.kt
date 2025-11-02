package com.ChickenKitchen.app.model.dto.request

data class CreateDishBaseRequest(
    val price: Int,
    val cal: Int,
    val note: String? = null,
    val isCustom: Boolean = false,
)

data class UpdateDishBaseRequest(
    val price: Int? = null,
    val cal: Int? = null,
    val note: String? = null,
    val isCustom: Boolean? = null,
)

