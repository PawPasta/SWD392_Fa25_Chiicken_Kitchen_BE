package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.dashboard.DashboardService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController
@RequestMapping("/api/dashboard")
class DashboardController (
    private val dashboardService : DashboardService,
){

    @Operation(summary = "Get store list with best performance")
    @GetMapping("/admin/best-performance")
    fun getBestStoresPerformance() : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(dashboardService.getBestStorePerformance(), "Fetched best store performance"))
    }

    @Operation(summary = "Get revenue trend for the last N days")
    @GetMapping("/admin/revenue-trend")
    fun getRevenueTrend(dayNumber : Int) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(dashboardService.getRevenueTrend(dayNumber), "Fetched revenue trend"))
    }

    @Operation(summary = "Get users trend from the last N days")
    @GetMapping("/admin/user-growth-trend")
    fun getUserGrowthTrend(dayNumber : Int) : ResponseEntity<ResponseModel>{
        return ResponseEntity.ok(ResponseModel.success(dashboardService.getUserGrowthTrend(dayNumber), "Fetched user growth trend"))
    }

    @Operation(summary = "Get summary report between two dates")
    @GetMapping("/manager/summary")
    fun getSummary(
        @RequestParam startDate: String,
        @RequestParam endDate: String
    ): ResponseEntity<ResponseModel> {
        val start = Timestamp.valueOf("${startDate} 00:00:00")
        val end = Timestamp.valueOf("${endDate} 23:59:59")
        val summary = dashboardService.getSummary(start, end)
        return ResponseEntity.ok(ResponseModel.success(summary))
    }

    @Operation(summary = "Get popular dishes between two dates")
    @GetMapping("/manager/popular-items")
    fun getPopularDishes(
        @RequestParam startDate: String,
        @RequestParam endDate: String,
        @RequestParam(defaultValue = "5") limit: Int
    ): ResponseEntity<ResponseModel> {
        val start = Timestamp.valueOf("${startDate} 00:00:00")
        val end = Timestamp.valueOf("${endDate} 23:59:59")
        val dishes = dashboardService.getPopularDishes(start, end, limit)
        return ResponseEntity.ok(ResponseModel.success(dishes, "Fetched popular dishes"))
    }

}