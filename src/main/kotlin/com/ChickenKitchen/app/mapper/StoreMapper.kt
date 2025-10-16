package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.StoreResponse
import com.ChickenKitchen.app.model.entity.ingredient.Store


fun Store.toStoreResponse() : StoreResponse =
    StoreResponse (
        id = this.id!!,
        address = this.address,
        createAt = this.createdAt,
        isActive = this.isActive,
        name = this.name,
        phone = this.phone
    )

fun List<Store>.toListStoreResponse() : List<StoreResponse> =
    this.map { it.toStoreResponse() }