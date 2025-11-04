package com.ChickenKitchen.app.service.payment

import com.ChickenKitchen.app.model.entity.order.Order

interface PaymentService {

    fun refundPayment(order: Order, reason: String? = null)

    fun rejectRefund(order: Order, reason: String? = null)

}