package com.Chicken_Kitchen.repository.order

import com.Chicken_Kitchen.model.entity.order.OrderStatusHistory
import com.Chicken_Kitchen.enum.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderStatusHistoryRepository : JpaRepository<OrderStatusHistory, Long> {
    fun findAllByOrderId(orderId: Long): List<OrderStatusHistory>
    fun findAllByStatus(status: OrderStatus): List<OrderStatusHistory>
}
