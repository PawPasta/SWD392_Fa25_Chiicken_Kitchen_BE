package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.model.entity.order.OrderCombo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderComboRepository : JpaRepository<OrderCombo, Long> {
    fun findAllByOrderId(orderId: Long): List<OrderCombo>
    fun findAllByComboId(comboId: Long): List<OrderCombo>
}
