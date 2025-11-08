package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.order.EmployeeOrderService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders/employee")
class OrderEmployeeController(
    private val employeeOrderService: EmployeeOrderService
) {
    // Removed legacy confirmed list endpoint

    @Operation(summary = "Employee: get CONFIRMED order detail by id")
    @GetMapping("/confirmed/{orderId}")
    fun getConfirmedOrderDetailForEmployee(@PathVariable orderId: Long): ResponseEntity<ResponseModel> {
        val result = employeeOrderService.getConfirmedOrderDetailForEmployee(orderId)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched confirmed order detail"))
    }

    @Operation(summary = "Employee: accept a CONFIRMED order -> PROCESSING")
    @PostMapping("/accept/{orderId}")
    fun employeeAcceptOrder(@PathVariable orderId: Long): ResponseEntity<ResponseModel> {
        val result = employeeOrderService.employeeAcceptOrder(orderId)
        return ResponseEntity.ok(ResponseModel.success(result, "Order moved to PROCESSING"))
    }

    @Operation(summary = "Employee: mark a PROCESSING order -> READY")
    @PostMapping("/ready/{orderId}")
    fun employeeMarkReadyOrder(@PathVariable orderId: Long): ResponseEntity<ResponseModel> {
        val result = employeeOrderService.employeeMarkReadyOrder(orderId)
        return ResponseEntity.ok(ResponseModel.success(result, "Order moved to READY"))
    }

    @Operation(summary = "Employee: complete a READY order -> COMPLETED")
    @PostMapping("/complete/{orderId}")
    fun employeeCompleteOrder(@PathVariable orderId: Long): ResponseEntity<ResponseModel> {
        val result = employeeOrderService.employeeCompleteOrder(orderId)
        return ResponseEntity.ok(ResponseModel.success(result, "Order moved to COMPLETED"))
    }

    @Operation(summary = "Employee: cancel an order")
    @PostMapping("/cancel/{orderId}")
    fun employeeCancelOrder(@PathVariable orderId: Long): ResponseEntity<ResponseModel> {
        val result = employeeOrderService.employeeCancelOrder(orderId)
        return ResponseEntity.ok(ResponseModel.success(result, "Order cancelled"))
    }

    @Operation(summary = "Employee: get my employee detail with store and user info")
    @GetMapping("/me")
    fun getMyEmployeeDetail(): ResponseEntity<ResponseModel> {
        val result = employeeOrderService.getMyEmployeeDetail()
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched employee detail"))
    }

    @Operation(summary = "Employee: list orders in my store by status (optional, excludes NEW); default by newest; supports pagination and keyword search")
    @GetMapping
    fun getOrdersByStatus(
        @RequestParam(required = false) status: String?,
        @RequestParam(name = "pageNumber", defaultValue = "1") pageNumber: Int,
        @RequestParam(name = "size", defaultValue = "10") size: Int,
        @RequestParam(name = "q", required = false) keyword: String?,
    ): ResponseEntity<ResponseModel> {
        val result = employeeOrderService.getOrdersForEmployeeStore(status, pageNumber, size, keyword)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched orders by status"))
    }

    @Operation(summary = "Employee: get order detail with required ingredients by order id")
    @GetMapping("/detail/{orderId}")
    fun getOrderDetailWithIngredients(@PathVariable orderId: Long): ResponseEntity<ResponseModel> {
        val result = employeeOrderService.getOrderDetailWithIngredients(orderId)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched order detail with ingredients"))
    }
}
