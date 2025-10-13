package com.ChickenKitchen.app.repository.menu

import com.ChickenKitchen.app.model.entity.menu.Nutrient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface NutrientRepository : JpaRepository<Nutrient, Long> {
    fun findByName(name: String): Nutrient?
}