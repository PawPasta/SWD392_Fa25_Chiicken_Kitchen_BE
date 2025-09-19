package com.Chicken_Kitchen.repository.recipe

import com.Chicken_Kitchen.model.entity.recipe.Recipe
import com.Chicken_Kitchen.enum.RecipeCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RecipeRepository : JpaRepository<Recipe, Long> {
    fun findByName(name: String): Optional<Recipe>
    fun findAllByIsActive(isActive: Boolean = true): List<Recipe>
    fun findAllByCategory(category: RecipeCategory): List<Recipe>
}
