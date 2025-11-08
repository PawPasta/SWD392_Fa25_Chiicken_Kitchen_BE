package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.model.dto.response.OrderBriefResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse
import com.ChickenKitchen.app.model.dto.response.EmployeeOrderListItemResponse
import com.ChickenKitchen.app.model.dto.response.EmployeeOrderDetailResponse

interface EmployeeOrderService {
    fun getConfirmedOrderDetailForEmployee(orderId: Long): OrderCurrentResponse
    fun employeeAcceptOrder(orderId: Long): OrderBriefResponse
    fun employeeMarkReadyOrder(orderId: Long): OrderBriefResponse
    fun employeeCompleteOrder(orderId: Long): OrderBriefResponse
    fun employeeCancelOrder(orderId: Long): OrderBriefResponse
    fun getMyEmployeeDetail(): com.ChickenKitchen.app.model.dto.response.EmployeeDetailFullResponse
    fun getOrdersForEmployeeStore(
        status: String?,
        pageNumber: Int,
        size: Int,
        keyword: String?
    ): com.ChickenKitchen.app.model.dto.response.EmployeeOrderListPageResponse
    fun getOrderDetailWithIngredients(orderId: Long): EmployeeOrderDetailResponse
}
