package com.ChickenKitchen.app.model.dto.response

import java.sql.Timestamp


data class StoreResponse (
    val id : Long,
    val name: String,
    val address: String,
    val phone: String,
    val isActive : Boolean,
    val createAt: Timestamp?,
)
