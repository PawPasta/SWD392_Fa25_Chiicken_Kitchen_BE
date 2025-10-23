package com.ChickenKitchen.app.repository.payment

import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.payment.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PaymentRepository : JpaRepository<Payment, Long> {

    fun findByOrderId(orderId: Long) : Payment?
    fun order(order: Order): MutableList<Payment>
}