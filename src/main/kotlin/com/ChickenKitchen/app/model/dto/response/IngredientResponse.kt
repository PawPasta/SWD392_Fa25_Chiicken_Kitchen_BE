package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.UnitType
import java.sql.Timestamp


data class StoreResponse (
    val id : Long,
    val name: String,
    val address: String,
    val phone: String,
    val isActive : Boolean,
    val createAt: Timestamp?,
)


data class StoreIngredientBatchResponse (
    val id: Long,
    val storeId: Long?,
    val ingredientId: Long?,
    val quantity: Long,
)


data class IngredientDetailResponse (
    val id: Long,
    val name: String?,
    val baseUnit: UnitType? = UnitType.G,
    val batchNumber: String,
    val imageUrl: String?,
    val isActive: Boolean,
    val createdAt: Timestamp?,
    val store: StoreResponse,
    val storeBatches: List<StoreIngredientBatchResponse>,
)