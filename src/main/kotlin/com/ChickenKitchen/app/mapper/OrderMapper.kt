package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.OrderDetailResponse
import com.ChickenKitchen.app.model.dto.response.OrderItemDetailResponse
import com.ChickenKitchen.app.model.dto.response.OrderResponse
import com.ChickenKitchen.app.model.entity.order.Order

fun Order.toOrderResponse() = OrderResponse(
    id = this.id!!,
    userId = this.user.id!!,
    totalPrice = this.totalPrice,
    status = this.status,
    createdAt = this.createdAt
)

fun Order.toOrderDetailResponse() = OrderDetailResponse(
    id = this.id!!,
    userId = this.user.id!!,
    totalPrice = this.totalPrice,
    status = this.status,
    createdAt = this.createdAt,
    items = this.order_items.map { oi ->
        OrderItemDetailResponse(
            dailyMenuItemId = oi.dailyMenuItem.id!!,
            quantity = oi.quantity,
            price = oi.price,
            cal = oi.cal,
            note = oi.note
        )
    }
)

fun List<Order>.toOrderResponseList() = this.map { it.toOrderResponse() }

