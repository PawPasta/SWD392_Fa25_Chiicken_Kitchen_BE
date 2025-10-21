package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.*
import com.ChickenKitchen.app.model.entity.ingredient.Store
import com.ChickenKitchen.app.model.entity.menu.DailyMenu

fun Store.toStoreMenuDailyResponse(): StoreDailyMenuResponse =
    StoreDailyMenuResponse(
        storeId = this.id!!,
        storeName = this.name
    )

fun DailyMenu.toDailyMenuResponse(): DailyMenuResponse =
    DailyMenuResponse(
        id = this.id!!,
        menuDate = this.menuDate
    )

fun DailyMenu.toDailyMenuDetailResponse(): DailyMenuDetailResponse {
    val storeList = this.stores.map { it.toStoreMenuDailyResponse() }

    val items = this.dailyMenuItems.map { it.menuItem }

    // Nhóm theo category
    val categoryList = items.groupBy { it.category.id!! }
        .map { (catId, list) ->
            DailyMenuCategoryGroupResponse(
                categoryId = catId,
                categoryName = list.first().category.name,
                items = list.map { it.toMenuItemResponse() }
            )
        }
        .sortedBy { it.categoryName }

    return DailyMenuDetailResponse(
        id = this.id!!,
        menuDate = this.menuDate,
        createdAt = this.createdAt,
        storeList = storeList,
        categoryList = categoryList
    )
}

fun List<DailyMenu>.toDailyMenuListResponse(): List<DailyMenuResponse> =
    this.map { it.toDailyMenuResponse() }
