package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.DishDetailResponse
import com.ChickenKitchen.app.model.dto.response.DishResponse
import com.ChickenKitchen.app.model.entity.step.Dish

fun Dish.toDishResponse(): DishResponse =
    DishResponse(
        id = this.id!!,
        price = this.price,
        cal = this.cal,
        isCustom = this.isCustom,
        note = this.note,
    )

fun Dish.toDishDetailResponse(): DishDetailResponse =
    DishDetailResponse(
        id = this.id!!,
        price = this.price,
        cal = this.cal,
        isCustom = this.isCustom,
        note = this.note,
        createdAt = this.createdAt?.toString() ?: "",
        updatedAt = this.updatedAt?.toString(),
    )

fun List<Dish>.toDishResponseList(): List<DishResponse> = this.map { it.toDishResponse() }

