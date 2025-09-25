package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.OrderStatus
import java.math.BigDecimal
import java.sql.Timestamp

data class OrderResponse(
    val id: Long,
    val userId: Long,
    val totalPrice: BigDecimal,
    val status: OrderStatus,
    val createdAt: Timestamp
)

data class OrderDetailResponse(
    val id: Long,
    val userId: Long,
    val totalPrice: BigDecimal,
    val status: OrderStatus,
    val createdAt: Timestamp,
    val items: List<OrderItemDetailResponse>
)

data class OrderItemDetailResponse(
    val dailyMenuItemId: Long,
    val quantity: Int,
    val price: BigDecimal,
    val cal: Int,
    val note: String?
)

