package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.order

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.order.OrderStatusHistory
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.enum.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderStatusHistoryRepository : JpaRepository<OrderStatusHistory, Long> {
    fun findAllByOrderId(orderId: Long): List<OrderStatusHistory>
    fun findAllByStatus(status: OrderStatus): List<OrderStatusHistory>
}
