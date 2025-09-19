package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.recipe

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.recipe.RecipeIngredient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeIngredientRepository : JpaRepository<RecipeIngredient, Long> {
    fun findAllByRecipeId(recipeId: Long): List<RecipeIngredient>
    fun findAllByIngredientId(ingredientId: Long): List<RecipeIngredient>
    fun findAllByCookingMethodId(cookingMethodId: Long): List<RecipeIngredient>
}
