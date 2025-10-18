package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enums.UnitType
import java.sql.Timestamp


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