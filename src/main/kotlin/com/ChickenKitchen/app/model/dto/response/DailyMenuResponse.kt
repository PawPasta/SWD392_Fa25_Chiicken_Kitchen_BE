package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.MenuType
import java.math.BigDecimal
import java.sql.Date

data class DailyMenuResponse(
    val id: Long,
    val date: Date,
    val name: String
)

data class DailyMenuDetailResponse(
    val id: Long,
    val date: Date,
    val name: String,
    val items: List<DailyMenuItemResponse>
)

data class DailyMenuItemResponse(
    val id: Long,
    val name: String,
    val menuType: MenuType,
    val refId: Long,
    val cal: Int,
    val price: BigDecimal
)
