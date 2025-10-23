package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.request.OrderConfirmRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.model.dto.response.DishDeleteResponse
import com.ChickenKitchen.app.service.order.OrderService
import com.ChickenKitchen.app.service.payment.VNPayService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService,
    private val vnPayService: VNPayService
) {
    @Operation(summary = "Add a dish to current NEW order (auto-create if none)")
    @PostMapping("/current/dishes")
    fun addDishToCurrentOrder(@RequestBody req: CreateDishRequest): ResponseEntity<ResponseModel> {
        val result = orderService.addDishToCurrentOrder(req)
        return ResponseEntity.ok(ResponseModel.success(result, "Dish added to order"))
    }

    @Operation(summary = "Get or create NEW order by store; clear items if not in today's daily menu")
    @GetMapping("/current")
    fun getCurrentOrderByStore(@RequestParam storeId: Long): ResponseEntity<ResponseModel> {
        val result = orderService.getCurrentOrderForStore(storeId)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched current order for store"))
    }

    @Operation(summary = "List user's orders (COMPLETED/CANCELLED/PROCESSING) by store")
    @GetMapping("/history")
    fun getOrderHistory(@RequestParam storeId: Long): ResponseEntity<ResponseModel> {
        val result = orderService.getOrdersHistory(storeId)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched order history"))
    }

    @Operation(summary = "Update a dish selections and note")
    @PutMapping("/dishes/{dishId}")
    fun updateDish(
        @PathVariable dishId: Long,
        @RequestBody req: UpdateDishRequest
    ): ResponseEntity<ResponseModel> {
        val result = orderService.updateDish(dishId, req)
        return ResponseEntity.ok(ResponseModel.success(result, "Dish updated"))
    }

    @Operation(summary = "Delete a dish from order")
    @DeleteMapping("/dishes/{dishId}")
    fun deleteDish(@PathVariable dishId: Long): ResponseEntity<ResponseModel> {
        val orderId = orderService.deleteDish(dishId)
        return ResponseEntity.ok(ResponseModel.success(DishDeleteResponse(orderId, dishId), "Dish deleted"))
    }

    @Operation(summary = "Confirm order (change to Confirmed), add promotion and paymentMethod")
    @PostMapping("/confirm")
    fun confirmOrder(@RequestBody req: OrderConfirmRequest ): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(orderService.confirmedOrder(req), "Confirmed on going"))
    }

    @Operation(summary = "vnpay-callback")
    @PostMapping("/vnpay-callback")
    fun vnpayCallback(@RequestBody params: Map<String, String>): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(vnPayService.callbackURL(params),"Nice"))

    }

}
