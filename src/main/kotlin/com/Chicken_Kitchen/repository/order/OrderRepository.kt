package com.Chicken_Kitchen.repository.order

import com.Chicken_Kitchen.model.entity.order.Order
import com.Chicken_Kitchen.enum.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findAllByUserId(userId: Long): List<Order>
    fun findAllByStatus(status: OrderStatus): List<Order>
}
