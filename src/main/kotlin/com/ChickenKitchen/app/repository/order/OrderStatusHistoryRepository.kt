package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.model.entity.order.OrderStatusHistory
import com.ChickenKitchen.app.enum.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderStatusHistoryRepository : JpaRepository<OrderStatusHistory, Long> {
    fun findAllByOrderId(orderId: Long): List<OrderStatusHistory>
    fun findAllByStatus(status: OrderStatus): List<OrderStatusHistory>
}
