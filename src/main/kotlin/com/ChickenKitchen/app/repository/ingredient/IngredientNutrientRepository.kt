package com.ChickenKitchen.app.repository.ingredient

import com.ChickenKitchen.app.model.entity.ingredient.IngredientNutrient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IngredientNutrientRepository : JpaRepository<IngredientNutrient, Long> {
    fun findAllByIngredientId(ingredientId: Long): List<IngredientNutrient>
    fun findAllByNutrientId(nutrientId: Long): List<IngredientNutrient>
}
