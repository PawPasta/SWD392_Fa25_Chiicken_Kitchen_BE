package com.ChickenKitchen.app.repository.cooking

import com.ChickenKitchen.app.model.entity.cooking.CookingEffect
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CookingEffectRepository : JpaRepository<CookingEffect, Long> {
    fun findAllByMethodId(methodId: Long): List<CookingEffect>
    fun findAllByNutrientId(nutrientId: Long): List<CookingEffect>
}
