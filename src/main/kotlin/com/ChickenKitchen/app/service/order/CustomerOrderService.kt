package com.ChickenKitchen.app.service.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.dto.request.CreateCustomDishRequest
import com.ChickenKitchen.app.model.dto.request.CreateExistingDishRequest
import com.ChickenKitchen.app.model.dto.request.CreateFeedbackRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishRequest
import com.ChickenKitchen.app.model.dto.response.AddDishResponse
import com.ChickenKitchen.app.model.dto.response.FeedbackResponse
import com.ChickenKitchen.app.model.dto.response.OrderBriefResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse
import com.ChickenKitchen.app.model.dto.response.OrderTrackingResponse
import com.ChickenKitchen.app.model.dto.response.CurrentDishResponse

interface CustomerOrderService {
    fun addExistingDishToCurrentOrder(req: CreateExistingDishRequest): AddDishResponse
    fun addCustomDishToCurrentOrder(req: CreateCustomDishRequest): AddDishResponse
    fun getCurrentOrderForStore(storeId: Long): OrderCurrentResponse
    fun getOrdersHistory(storeId: Long): List<OrderBriefResponse>
    fun updateDish(dishId: Long, req: UpdateDishRequest): AddDishResponse
    fun deleteDish(dishId: Long): Long
    // Remove link of an existing (non-custom) dish from current NEW order in a store
    fun deleteExistingDishLink(storeId: Long, dishId: Long): Long

    fun getAllOrderStatuses(): List<OrderStatus>

    fun createFeedback(orderId: Long, req: CreateFeedbackRequest): FeedbackResponse
    fun getFeedbackByOrder(orderId: Long): FeedbackResponse

    fun getOrderTracking(orderId: Long): OrderTrackingResponse
    fun customerCancelOrder(orderId: Long, reason : String?): OrderTrackingResponse?

    // Previously ordered dishes for quick reorder (includes custom)
    fun getPreviouslyOrderedDishes(storeId: Long): List<CurrentDishResponse>

    // Update quantity for a dish (custom or existing) in current cart, scoped by store
    fun updateDishQuantity(dishId: Long, storeId: Long, quantity: Int): AddDishResponse

}
