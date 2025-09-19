package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.ingredient

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.ingredient.Nutrient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NutrientRepository : JpaRepository<Nutrient, Long> {
    fun findByName(name: String): Optional<Nutrient>
}
