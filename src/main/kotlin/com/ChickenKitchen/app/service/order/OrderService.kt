package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.response.AddDishResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse

interface OrderService {
    fun addDishToCurrentOrder(req: CreateDishRequest): AddDishResponse
    fun getCurrentOrderForStore(storeId: Long): OrderCurrentResponse
}
