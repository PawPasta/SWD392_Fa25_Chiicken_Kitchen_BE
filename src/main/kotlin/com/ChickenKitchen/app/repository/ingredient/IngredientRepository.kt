package com.ChickenKitchen.app.repository.ingredient

import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IngredientRepository : JpaRepository<Ingredient, Long> {
    fun findByName(name: String): Optional<Ingredient>
    fun findAllByIsActive(isActive: Boolean = true): List<Ingredient>
}
