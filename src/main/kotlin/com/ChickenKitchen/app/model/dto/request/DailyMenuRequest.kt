package com.ChickenKitchen.app.model.dto.request

import java.sql.Timestamp


data class CreateDailyMenuRequest (
    val menuItemIds : List<Long>,
    val menuDate : Timestamp,
)

data class UpdateDailyMenuRequest (
    val storeIds: List<Long>?,
    val menuItemIds : List<Long>?,
    val menuDate : Timestamp?,
)