package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.model.entity.order.OrderStepItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderStepItemRepository : JpaRepository<OrderStepItem, Long> {
    fun countByDailyMenuItemMenuItemId(menuItemId: Long): Long
}

