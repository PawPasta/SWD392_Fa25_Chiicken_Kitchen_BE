package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.enums.UnitType
import com.ChickenKitchen.app.model.dto.response.IngredientDetailResponse
import com.ChickenKitchen.app.model.dto.response.IngredientResponse
//import com.ChickenKitchen.app.model.dto.response.StoreIngredientBatchResponse
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
//import com.ChickenKitchen.app.model.entity.ingredient.StoreIngredientBatch


// === CHUA CO Y DINH DUNG TOI BATCH

//fun StoreIngredientBatch.toStoreIngredientBatchResponse() : StoreIngredientBatchResponse =
//    StoreIngredientBatchResponse(
//        id = this.id!!,
//        ingredientId = this.ingredient.id,
//        quantity = this.quantity,
//        storeId = this.store.id,
//        storeName = this.store.name
//    )

fun Ingredient.toIngredientResponse() : IngredientResponse =
    IngredientResponse (
        id = this.id!!,
        name = this.name,
        imageUrl = this.imageUrl,
        description = this.description
    )

fun Ingredient.toIngredientDetailResponse(): IngredientDetailResponse {
//    val firstBatch = this.storeBatches.firstOrNull()
//        ?: throw IllegalStateException("Ingredient ${this.id} has no store batches")

//    val batchResponses = this.storeBatches.map { it.toStoreIngredientBatchResponse() }

    return IngredientDetailResponse(
        id = this.id!!,
        name = this.name,
        baseUnit = this.baseUnit ?: UnitType.G,
        batchNumber = this.batchNumber,
        imageUrl = this.imageUrl,
        isActive = this.isActive,
        createdAt = this.createdAt,
        description = this.description
//        storeBatches = batchResponses,
    )
}

fun List<Ingredient>.toListIngredientResponse() : List<IngredientResponse> =
    this.map { it.toIngredientResponse() }