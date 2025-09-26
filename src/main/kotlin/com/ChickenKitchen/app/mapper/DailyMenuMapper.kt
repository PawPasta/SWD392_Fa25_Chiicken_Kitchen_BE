package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.DailyMenuDetailResponse
import com.ChickenKitchen.app.model.dto.response.DailyMenuItemResponse
import com.ChickenKitchen.app.model.dto.response.DailyMenuResponse
import com.ChickenKitchen.app.model.entity.menu.DailyMenu

fun DailyMenu.toDailyMenuResponse() = DailyMenuResponse(
    id = this.id!!,
    date = this.date,
    name = this.name
)

fun DailyMenu.toDailyMenuDetailResponse() = DailyMenuDetailResponse(
    id = this.id!!,
    date = this.date,
    name = this.name,
    items = this.dailyMenuItems.map { item ->
        DailyMenuItemResponse(
            id = item.id!!,
            name = item.name,
            menuType = item.menuType,
            refId = item.refId,
            cal = item.cal,
            price = item.price
        )
    }
)

fun List<DailyMenu>.toDailyMenuResponseList() = this.map { it.toDailyMenuResponse() }
