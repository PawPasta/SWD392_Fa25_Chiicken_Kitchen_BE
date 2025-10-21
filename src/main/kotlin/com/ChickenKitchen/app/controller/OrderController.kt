package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.order.OrderService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
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
}
