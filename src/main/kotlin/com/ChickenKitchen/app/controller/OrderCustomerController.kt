package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.dto.request.CreateCustomDishRequest
import com.ChickenKitchen.app.model.dto.request.CreateExistingDishRequest
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
    @Operation(summary = "Add an existing dish (non-custom) to current NEW order with quantity")
    @PostMapping("/current/dishes/existing")
    fun addExistingDish(@RequestBody req: CreateExistingDishRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(customerOrderService.addExistingDishToCurrentOrder(req), "Existing dish added"))

    @Operation(summary = "Add a custom dish (by steps) to current NEW order")
    @PostMapping("/current/dishes/custom")
    fun addCustomDish(@RequestBody req: CreateCustomDishRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(customerOrderService.addCustomDishToCurrentOrder(req), "Custom dish added"))

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


    @Operation(summary = "Cancelled Order (for customer)")
    @PostMapping("/api/orders/{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: Long): ResponseEntity<ResponseModel> {
        val res = customerOrderService.customerCancelOrder(orderId)
        return ResponseEntity.ok(ResponseModel.success(res, "Order cancelled"))
    }
}
