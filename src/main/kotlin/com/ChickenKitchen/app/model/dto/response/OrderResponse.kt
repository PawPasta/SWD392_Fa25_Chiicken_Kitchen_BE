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

// Response model for user's own orders with item details

data class UserOrderResponse(
    val id: Long,
    val totalPrice: BigDecimal,
    val status: OrderStatus,
    val createdAt: Timestamp,
    val items: List<UserOrderItemDetailResponse>,
    val address: UserAddressResponse? = null
)

data class UserOrderDetailResponse(
    val id: Long,
    val totalPrice: BigDecimal,
    val status: OrderStatus,
    val createdAt: Timestamp,
    val items: List<UserOrderItemDetailResponse>,
    val address: UserAddressResponse? = null
)

data class UserOrderItemResponse(
    val dailyMenuItemId: Long,
    val name: String,
    val imageUrl: String,
    val quantity: Int,
    val price: BigDecimal,
)

data class UserOrderItemDetailResponse(
    val dailyMenuItemId: Long,
    val name: String,
    val imageUrl: String,
    val quantity: Int,
    val price: BigDecimal,
    val cal: Int,
    val note: String?
)

// Use existing UserAddressResponse in this package (see UserAddressResponse.kt)
