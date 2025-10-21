package com.ChickenKitchen.app.model.dto.response

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

