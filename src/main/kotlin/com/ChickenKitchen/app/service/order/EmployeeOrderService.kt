package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.model.dto.response.OrderBriefResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse

interface EmployeeOrderService {
    fun getConfirmedOrdersForEmployeeStore(): List<OrderBriefResponse>
    fun getConfirmedOrderDetailForEmployee(orderId: Long): OrderCurrentResponse
    fun employeeAcceptOrder(orderId: Long): OrderBriefResponse
    fun employeeMarkReadyOrder(orderId: Long): OrderBriefResponse
    fun employeeCompleteOrder(orderId: Long): OrderBriefResponse
}

