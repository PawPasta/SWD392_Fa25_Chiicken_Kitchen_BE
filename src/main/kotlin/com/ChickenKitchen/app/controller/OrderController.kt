package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateOrderRequest
import com.ChickenKitchen.app.model.dto.request.UpdateOrderRequest
import com.ChickenKitchen.app.model.dto.request.AddOrderItemRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.order.OrderService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/order")
class OrderController(
    private val orderService: OrderService
) {

    // User endpoints
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get orders of the authenticated user")
    @GetMapping("/user")
    fun getUserOrders(): ResponseEntity<ResponseModel> {
        val orders = orderService.getUserOrders()
        return ResponseEntity.ok(ResponseModel.success(orders, "Get user orders successfully!"))
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order by ID for the authenticated user")
    @GetMapping("/user/{id}")
    fun getUserOrderById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val order = orderService.getUserOrderById(id)
        return ResponseEntity.ok(ResponseModel.success(order, "Get user order successfully!"))
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add item to order for the authenticated user")
    @PostMapping("/user")
    fun createUserOrder(@RequestBody req: AddOrderItemRequest): ResponseEntity<ResponseModel> {
        val created = orderService.addOrderItem(req)
        return ResponseEntity.ok(ResponseModel.success(created, "Create user order successfully!"))
    }

    // Admin-only endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders (Admin only)")
    @GetMapping("")
    fun getAllOrders(): ResponseEntity<ResponseModel> {
        val orders = orderService.getAll()
        return ResponseEntity.ok(ResponseModel.success(orders, "Get all orders successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get order by ID (Admin only)")
    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val order = orderService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(order, "Get order successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new order (Admin only)")
    @PostMapping("")
    fun createOrder(@RequestBody req: CreateOrderRequest): ResponseEntity<ResponseModel> {
        val created = orderService.create(req)
        return ResponseEntity.ok(ResponseModel.success(created, "Create order successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order (Admin only)")
    @PutMapping("/{id}")
    fun updateOrder(@PathVariable id: Long, @RequestBody req: UpdateOrderRequest): ResponseEntity<ResponseModel> {
        val updated = orderService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(updated, "Update order successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete order (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        orderService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete order successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change order status by toggle (Admin only)")
    @PatchMapping("/{id}/status")
    fun changeOrderStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val order = orderService.changeStatus(id)
        return ResponseEntity.ok(ResponseModel.success(order, "Change order status successfully!"))
    }
}

