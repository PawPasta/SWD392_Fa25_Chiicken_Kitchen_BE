package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.request.OrderConfirmRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishRequest
import com.ChickenKitchen.app.model.dto.response.AddDishResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse
import com.ChickenKitchen.app.model.dto.response.OrderBriefResponse

interface OrderService {
    fun addDishToCurrentOrder(req: CreateDishRequest): AddDishResponse
    fun getCurrentOrderForStore(storeId: Long): OrderCurrentResponse
    fun getOrdersHistory(storeId: Long): List<OrderBriefResponse>
    fun updateDish(dishId: Long, req: UpdateDishRequest): AddDishResponse
    fun deleteDish(dishId: Long): Long

    fun confirmedOrder(req: OrderConfirmRequest) : String

    fun getConfirmedOrdersForEmployeeStore(): List<OrderBriefResponse>

    fun getConfirmedOrderDetailForEmployee(orderId: Long): OrderCurrentResponse

    fun employeeAcceptOrder(orderId: Long): OrderBriefResponse
    fun employeeMarkReadyOrder(orderId: Long): OrderBriefResponse
    fun employeeCompleteOrder(orderId: Long): OrderBriefResponse
}
