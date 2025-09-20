package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.cooking.CookingMethod
import com.ChickenKitchen.app.model.dto.response.CookingMethodResponse
import com.ChickenKitchen.app.model.dto.response.CookingMethodDetailResponse

fun CookingMethod.toCookingMethodResponse() = CookingMethodResponse(
    id = this.id!!,
    name = this.name,
    basePrice = this.basePrice
)

fun CookingMethod.toCookingMethodDetailResponse() = CookingMethodDetailResponse(
    id = this.id!!,
    name = this.name,
    note = this.note,
    basePrice = this.basePrice,
)

fun List<CookingMethod>.toCookingMethodResponseList() = this.map { it.toCookingMethodResponse() }
