package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.request.CreateFeedbackRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishRequest
import com.ChickenKitchen.app.model.dto.response.DishDeleteResponse
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.order.CustomerOrderService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderCustomerController(
    private val customerOrderService: CustomerOrderService
) {
    @Operation(summary = "Add a dish to current NEW order (auto-create if none)")
    @PostMapping("/current/dishes")
    fun addDishToCurrentOrder(@RequestBody req: CreateDishRequest): ResponseEntity<ResponseModel> {
        val result = customerOrderService.addDishToCurrentOrder(req)
        return ResponseEntity.ok(ResponseModel.success(result, "Dish added to order"))
    }

    @Operation(summary = "Get or create NEW order by store; clear items if not in today's daily menu")
    @GetMapping("/current")
    fun getCurrentOrderByStore(@RequestParam storeId: Long): ResponseEntity<ResponseModel> {
        val result = customerOrderService.getCurrentOrderForStore(storeId)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched current order for store"))
    }

    @Operation(summary = "List user's orders (COMPLETED/CANCELLED/PROCESSING) by store")
    @GetMapping("/history")
    fun getOrderHistory(@RequestParam storeId: Long): ResponseEntity<ResponseModel> {
        val result = customerOrderService.getOrdersHistory(storeId)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched order history"))
    }

    @Operation(summary = "Update a dish selections and note")
    @PutMapping("/dishes/{dishId}")
    fun updateDish(
        @PathVariable dishId: Long,
        @RequestBody req: UpdateDishRequest
    ): ResponseEntity<ResponseModel> {
        val result = customerOrderService.updateDish(dishId, req)
        return ResponseEntity.ok(ResponseModel.success(result, "Dish updated"))
    }

    @Operation(summary = "Delete a dish from order")
    @DeleteMapping("/dishes/{dishId}")
    fun deleteDish(@PathVariable dishId: Long): ResponseEntity<ResponseModel> {
        val orderId = customerOrderService.deleteDish(dishId)
        return ResponseEntity.ok(ResponseModel.success(DishDeleteResponse(orderId, dishId), "Dish deleted"))
    }

    @Operation(summary = "List all order statuses")
    @GetMapping("/statuses")
    fun getAllOrderStatuses(): ResponseEntity<ResponseModel> {
        val statuses: List<OrderStatus> = customerOrderService.getAllOrderStatuses()
        return ResponseEntity.ok(ResponseModel.success(statuses, "Fetched order statuses"))
    }

    @Operation(summary = "Create feedback for a COMPLETED order (owner only)")
    @PostMapping("/{orderId}/feedback")
    fun createFeedback(
        @PathVariable orderId: Long,
        @RequestBody req: CreateFeedbackRequest
    ): ResponseEntity<ResponseModel> {
        val result = customerOrderService.createFeedback(orderId, req)
        return ResponseEntity.ok(ResponseModel.success(result, "Feedback created"))
    }

    @Operation(summary = "Get feedback by order id")
    @GetMapping("/{orderId}/feedback")
    fun getFeedbackByOrder(@PathVariable orderId: Long): ResponseEntity<ResponseModel> {
        val result = customerOrderService.getFeedbackByOrder(orderId)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched feedback"))
    }

    @Operation(summary = "Track order progress by order id")
    @GetMapping("/{orderId}/tracking")
    fun getOrderTracking(@PathVariable orderId: Long): ResponseEntity<ResponseModel> {
        val result = customerOrderService.getOrderTracking(orderId)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched order tracking"))
    }
}
