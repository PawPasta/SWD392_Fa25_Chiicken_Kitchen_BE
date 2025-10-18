package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enums.UnitType

import java.sql.Timestamp


data class CreateIngredientRequest(
    val name: String,
    val baseUnit: UnitType,
    val createAt: Timestamp,
    val imageUrl: String,
    val isActive: Boolean,
    val storeIds: List<Long>, // đổi từ storeId: Long -> storeIds: List<Long>
    val batchNumber: String,
    val quantity: Long,
)

data class UpdateIngredientRequest (
    val name: String?,
    val imageUrl: String?,
    val baseUnit: UnitType,
    val isActive: Boolean,
    val batchNumber: String?,
    val quantity: Long?,
)

