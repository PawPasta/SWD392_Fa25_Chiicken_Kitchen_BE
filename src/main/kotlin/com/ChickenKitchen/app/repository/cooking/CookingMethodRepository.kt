package com.ChickenKitchen.app.repository.cooking

import com.ChickenKitchen.app.model.entity.cooking.CookingMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CookingMethodRepository : JpaRepository<CookingMethod, Long> {
    fun findByName(name: String): Optional<CookingMethod>
    fun findAllByOrderByNameAsc(): List<CookingMethod>
}
