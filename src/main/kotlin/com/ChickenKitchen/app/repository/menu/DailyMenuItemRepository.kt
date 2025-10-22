package com.ChickenKitchen.app.repository.menu

import com.ChickenKitchen.app.model.entity.menu.DailyMenuItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DailyMenuItemRepository : JpaRepository<DailyMenuItem, Long>
