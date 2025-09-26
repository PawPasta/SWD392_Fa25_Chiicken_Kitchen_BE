package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.model.dto.request.CreateOrderRequest
import com.ChickenKitchen.app.model.dto.request.UpdateOrderRequest
import com.ChickenKitchen.app.model.dto.response.OrderDetailResponse
import com.ChickenKitchen.app.model.dto.response.OrderResponse
import com.ChickenKitchen.app.model.dto.response.UserOrderResponse
import com.ChickenKitchen.app.model.dto.response.UserOrderDetailResponse
import com.ChickenKitchen.app.model.dto.request.AddOrderItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserOrderItemRequest
import com.ChickenKitchen.app.model.dto.request.ConfirmOrderRequest
import com.ChickenKitchen.app.service.BaseService

interface OrderService : BaseService<OrderResponse, OrderDetailResponse,
        CreateOrderRequest, UpdateOrderRequest, Long> {
    fun changeStatus(id: Long): OrderResponse
    fun getUserOrders(): List<UserOrderResponse>
    fun getUserOrderById(id: Long): UserOrderDetailResponse
    fun addOrderItem(req: AddOrderItemRequest): UserOrderDetailResponse
    fun updateUserOrderItem(orderId: Long, dailyMenuItemId: Long, req: UpdateUserOrderItemRequest): UserOrderDetailResponse?
    fun deleteUserOrderItem(orderId: Long, dailyMenuItemId: Long)
    fun confirmUserOrder(orderId: Long, req: ConfirmOrderRequest): UserOrderDetailResponse
}
