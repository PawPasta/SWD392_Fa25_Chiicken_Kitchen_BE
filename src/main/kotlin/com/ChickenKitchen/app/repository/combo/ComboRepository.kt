package com.ChickenKitchen.app.repository.combo

import com.ChickenKitchen.app.model.entity.combo.Combo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ComboRepository : JpaRepository<Combo, Long> {
    fun findByName(name: String): Optional<Combo>
    fun findAllByIsActive(isActive: Boolean = true): List<Combo>
}
