package com.ChickenKitchen.app.repository.menu

import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface DailyMenuRepository : JpaRepository<DailyMenu, Long> {

    fun existsByMenuDate(menuDate: Timestamp): Boolean
}