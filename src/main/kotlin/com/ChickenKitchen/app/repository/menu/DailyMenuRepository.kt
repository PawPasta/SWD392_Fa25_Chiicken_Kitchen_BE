package com.ChickenKitchen.app.repository.menu

import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.sql.Date
import java.util.*

@Repository
interface DailyMenuRepository : JpaRepository<DailyMenu, Long> {
    fun findByDate(date: Date): Optional<DailyMenu>
}

