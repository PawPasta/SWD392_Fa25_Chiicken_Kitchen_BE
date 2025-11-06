package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.model.dto.request.OrderConfirmRequest
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod

interface OrderPaymentService {

    fun confirmOrder(req: OrderConfirmRequest): String
    fun processPayment(order: Order, paymentMethod: PaymentMethod, amount: Int, channel: String? = null): String
}
