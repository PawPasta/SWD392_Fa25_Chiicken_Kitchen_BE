package com.ChickenKitchen.app.repository.ingredient

import com.ChickenKitchen.app.model.entity.ingredient.StoreIngredientBatch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreIngredientBatchRepository : JpaRepository<StoreIngredientBatch, Long> {

}