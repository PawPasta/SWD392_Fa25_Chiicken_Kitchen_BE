package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.model.dto.request.CreateOrderRequest
import com.ChickenKitchen.app.model.dto.request.UpdateOrderRequest
import com.ChickenKitchen.app.model.dto.response.OrderDetailResponse
import com.ChickenKitchen.app.model.dto.response.OrderResponse
import com.ChickenKitchen.app.service.BaseService

interface OrderService : BaseService<OrderResponse, OrderDetailResponse,
        CreateOrderRequest, UpdateOrderRequest, Long> {
    fun changeStatus(id: Long): OrderResponse
}

