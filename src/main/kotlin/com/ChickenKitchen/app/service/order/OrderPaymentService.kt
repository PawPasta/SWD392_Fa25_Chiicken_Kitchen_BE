package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.model.dto.request.OrderConfirmRequest

interface OrderPaymentService {
    fun confirmOrder(req: OrderConfirmRequest): String
}

