package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.order

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.order.OrderCombo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderComboRepository : JpaRepository<OrderCombo, Long> {
    fun findAllByOrderId(orderId: Long): List<OrderCombo>
    fun findAllByComboId(comboId: Long): List<OrderCombo>
}
