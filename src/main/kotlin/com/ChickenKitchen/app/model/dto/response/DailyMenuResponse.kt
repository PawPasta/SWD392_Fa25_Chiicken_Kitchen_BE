package com.ChickenKitchen.app.model.dto.response

import java.sql.Timestamp


data class DailyMenuResponse(
    val id: Long,
    val menuDate: Timestamp?,
    val createdAt: Timestamp?,
    val storeList: List<StoreDailyMenuResponse>,
    val itemList: List<ItemDailyMenuResponse>
)

data class StoreDailyMenuResponse(
    val storeId: Long,
    val storeName: String
)

data class ItemDailyMenuResponse(
    val menuItemId: Long,
    val name: String,
    val category : CategoriesItemDailyMenuResponse
)

data class CategoriesItemDailyMenuResponse(
    val categoryId : Long,
    val name: String,
)