package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.enum.UnitType
import com.ChickenKitchen.app.model.dto.response.IngredientDetailResponse
import com.ChickenKitchen.app.model.dto.response.StoreIngredientBatchResponse
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.model.entity.ingredient.StoreIngredientBatch

fun StoreIngredientBatch.toStoreIngredientBatchResponse() : StoreIngredientBatchResponse =
    StoreIngredientBatchResponse(
        id = this.id!!,
        ingredientId = this.ingredient.id,
        quantity = this.quantity,
        storeId = this.store.id,

    )

fun Ingredient.toIngredientDetailResponse(): IngredientDetailResponse {
    val firstBatch = this.storeBatches.firstOrNull()
        ?: throw IllegalStateException("Ingredient ${this.id} has no store batches")

    val mainStore = firstBatch.store.toStoreResponse()

    val batchResponses = this.storeBatches.map { it.toStoreIngredientBatchResponse() }

    return IngredientDetailResponse(
        id = this.id!!,
        name = this.name,
        baseUnit = this.baseUnit ?: UnitType.G,
        batchNumber = this.batchNumber,
        imageUrl = this.imageUrl,
        isActive = this.isActive,
        createdAt = this.createdAt,
        store = mainStore,
        storeBatches = batchResponses,
    )
}

fun List<Ingredient>.toListIngredientResponse() : List<IngredientDetailResponse> =
    this.map { it.toIngredientDetailResponse() }