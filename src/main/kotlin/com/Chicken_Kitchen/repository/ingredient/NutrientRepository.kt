package com.Chicken_Kitchen.repository.ingredient

import com.Chicken_Kitchen.model.entity.ingredient.Nutrient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NutrientRepository : JpaRepository<Nutrient, Long> {
    fun findByName(name: String): Optional<Nutrient>
}
