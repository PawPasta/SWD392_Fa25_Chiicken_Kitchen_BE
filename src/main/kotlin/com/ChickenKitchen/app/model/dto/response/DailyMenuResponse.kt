package com.ChickenKitchen.app.model.dto.response

import java.sql.Timestamp


data class DailyMenuResponse(
    val id: Long,
    val menuDate: Timestamp?,
)

data class DailyMenuDetailResponse (
    val id: Long,
    val menuDate: Timestamp?,
    val createdAt: Timestamp?,
    val storeList: List<StoreDailyMenuResponse>,
    val categoryList: List<DailyMenuCategoryGroupResponse>
)

data class StoreDailyMenuResponse(
    val storeId: Long,
    val storeName: String
)

data class DailyMenuCategoryGroupResponse(
    val categoryId: Long,
    val categoryName: String,
    val items: List<MenuItemResponse>
)

data class DailyMenuByStoreResponse(
    val storeId: Long,
    val storeName: String,
    val menuDate: String,
    val categories: List<DailyMenuCategoryGroupResponse>
)