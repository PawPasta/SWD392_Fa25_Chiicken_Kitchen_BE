package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.OrderStatus

data class CreateOrderRequest(
    val userId: Long,
    val status: OrderStatus = OrderStatus.PENDING,
    val items: List<OrderItemRequest>
)

data class UpdateOrderRequest(
    val status: OrderStatus? = null,
    val items: List<OrderItemRequest>? = null
)

data class OrderItemRequest(
    val dailyMenuItemId: Long,
    val quantity: Int,
    val note: String? = null
)

// User

data class AddOrderItemRequest(
    val dailyMenuItemId: Long,
    val quantity: Int,
    val note: String? = null
) 

data class UpdateUserOrderItemRequest(
    val quantity: Int,
    val note: String? = null
)
