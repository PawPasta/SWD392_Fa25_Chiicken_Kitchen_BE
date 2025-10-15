package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.response.AddDishResponse

interface OrderService {
    fun addDishToCurrentOrder(req: CreateDishRequest): AddDishResponse
}

