package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.entity.order.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long>{
    fun findFirstByUserEmailAndStoreIdAndStatusOrderByCreatedAtDesc(email: String, storeId: Long, status: OrderStatus): Order?

    fun findAllByUserEmailAndStoreIdAndStatusInOrderByCreatedAtDesc(
        email: String,
        storeId: Long,
        statuses: List<OrderStatus>
    ): List<Order>

    fun findAllByStoreIdAndStatusOrderByCreatedAtDesc(
        storeId: Long,
        status: OrderStatus
    ): List<Order>
}
