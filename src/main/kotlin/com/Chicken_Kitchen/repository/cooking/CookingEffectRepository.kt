package com.Chicken_Kitchen.repository.cooking

import com.Chicken_Kitchen.model.entity.cooking.CookingEffect
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CookingEffectRepository : JpaRepository<CookingEffect, Long> {
    fun findAllByMethodId(methodId: Long): List<CookingEffect>
    fun findAllByNutrientId(nutrientId: Long): List<CookingEffect>
}
