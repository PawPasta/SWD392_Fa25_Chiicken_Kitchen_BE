package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.cooking

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.cooking.CookingEffect
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CookingEffectRepository : JpaRepository<CookingEffect, Long> {
    fun findAllByMethodId(methodId: Long): List<CookingEffect>
    fun findAllByNutrientId(nutrientId: Long): List<CookingEffect>
}
