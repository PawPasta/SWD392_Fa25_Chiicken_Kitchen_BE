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
    @Operation(summary = "Employee: list CONFIRMED orders in employee's store")
    @GetMapping("/confirmed")
    fun getConfirmedOrdersForEmployeeStore(): ResponseEntity<ResponseModel> {
        val result = employeeOrderService.getConfirmedOrdersForEmployeeStore()
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched confirmed orders for employee store"))
    }

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
}

