package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.ingredient

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.ingredient.IngredientNutrient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IngredientNutrientRepository : JpaRepository<IngredientNutrient, Long> {
    fun findAllByIngredientId(ingredientId: Long): List<IngredientNutrient>
    fun findAllByNutrientId(nutrientId: Long): List<IngredientNutrient>
}
