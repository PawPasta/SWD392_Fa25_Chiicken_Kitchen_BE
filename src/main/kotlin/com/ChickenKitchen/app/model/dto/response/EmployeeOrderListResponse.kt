package com.ChickenKitchen.app.model.dto.response

import java.sql.Timestamp

data class OrderCustomerResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val imageURL: String?,
)

data class EmployeeOrderListItemResponse(
    val orderId: Long,
    val status: String,
    val totalPrice: Int,
    val createdAt: Timestamp?,
    val pickupTime: Timestamp?,
    val customer: OrderCustomerResponse,
    val dishes: List<CurrentDishResponse>,
)

data class EmployeeOrderListPageResponse(
    val items: List<EmployeeOrderListItemResponse>,
    val total: Long,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
)
