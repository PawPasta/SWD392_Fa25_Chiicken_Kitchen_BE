package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.MenuType
import java.math.BigDecimal
import java.sql.Date

data class CreateDailyMenuRequest(
    val date: Date,
    val name: String,
    val items: List<DailyMenuItemRequest>? = null
)

data class UpdateDailyMenuRequest(
    val date: Date? = null,
    val name: String? = null,
    val items: List<DailyMenuItemRequest>? = null
)

data class DailyMenuItemRequest(
    val name: String,
    val menuType: MenuType,
    val refId: Long,
    val cal: Int,
    val price: BigDecimal
)
