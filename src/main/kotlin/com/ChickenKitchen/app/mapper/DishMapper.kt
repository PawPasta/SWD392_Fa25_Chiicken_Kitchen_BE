package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.DishDetailResponse
import com.ChickenKitchen.app.model.dto.response.DishStepItemResponse
import com.ChickenKitchen.app.model.dto.response.DishStepResponse
import com.ChickenKitchen.app.model.dto.response.DishResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemNutrientBriefResponse
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.model.entity.order.OrderStep

fun Dish.toDishResponse(): DishResponse =
    DishResponse(
        id = this.id!!,
        name = this.name,
        price = this.price,
        cal = this.cal,
        isCustom = this.isCustom,
        note = this.note,
        imageUrl = this.imageUrl,
    )

fun Dish.toDishDetailResponse(): DishDetailResponse =
    DishDetailResponse(
        id = this.id!!,
        name = this.name,
        price = this.price,
        cal = this.cal,
        isCustom = this.isCustom,
        note = this.note,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt?.toString() ?: "",
        updatedAt = this.updatedAt?.toString(),
    )

fun Dish.toDishDetailResponse(steps: List<OrderStep>, nutrients: List<MenuItemNutrientBriefResponse> = emptyList()): DishDetailResponse {
    val stepDtos = steps.map { os ->
        val items = os.items.map { link ->
            val mi = link.menuItem
            DishStepItemResponse(
                menuItemId = mi.id!!,
                name = mi.name,
                imageUrl = mi.imageUrl,
                quantity = link.quantity,
                price = mi.price,
                cal = mi.cal
            )
        }
        DishStepResponse(
            stepId = os.step.id!!,
            stepName = os.step.name,
            items = items
        )
    }
    return DishDetailResponse(
        id = this.id!!,
        name = this.name,
        price = this.price,
        cal = this.cal,
        isCustom = this.isCustom,
        note = this.note,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt?.toString() ?: "",
        updatedAt = this.updatedAt?.toString(),
        steps = stepDtos,
        nutrients = nutrients
    )
}

fun List<Dish>.toDishResponseList(): List<DishResponse> = this.map { it.toDishResponse() }
