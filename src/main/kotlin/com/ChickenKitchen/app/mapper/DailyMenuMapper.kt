package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.CategoriesItemDailyMenuResponse
import com.ChickenKitchen.app.model.dto.response.DailyMenuResponse
import com.ChickenKitchen.app.model.dto.response.ItemDailyMenuResponse
import com.ChickenKitchen.app.model.dto.response.StoreDailyMenuResponse
import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.model.entity.ingredient.Store
import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import com.ChickenKitchen.app.model.entity.menu.MenuItem


fun Store.toStoreMenuDailyResponse() : StoreDailyMenuResponse =
    StoreDailyMenuResponse(
        storeId = this.id!!,
        storeName = this.name,
    )

fun Category.toCategoryItemDailyMenuResponse() : CategoriesItemDailyMenuResponse =
    CategoriesItemDailyMenuResponse(
    categoryId = this.id!!,
    name = this.name
)

fun MenuItem.toItemDailyMenuResponse() : ItemDailyMenuResponse =
    ItemDailyMenuResponse(
        menuItemId = this.id!!,
        name = this.name,
        category = this.category.toCategoryItemDailyMenuResponse()
    )


fun DailyMenu.toDailyMenuResponse() : DailyMenuResponse =
    DailyMenuResponse(
    id = this.id!!,
    menuDate = this.menuDate,
    createdAt = this.createdAt,
    storeList = this.stores.map { it.toStoreMenuDailyResponse() },
    itemList = this.dailyMenuItems.map { it.menuItem.toItemDailyMenuResponse() },
)


fun List<DailyMenu>.toDailyMenuListResponse() : List<DailyMenuResponse> =
    this.map { it.toDailyMenuResponse() }