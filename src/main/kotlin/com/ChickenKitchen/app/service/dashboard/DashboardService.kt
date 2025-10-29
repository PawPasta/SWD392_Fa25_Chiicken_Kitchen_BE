package com.ChickenKitchen.app.service.dashboard

import com.ChickenKitchen.app.model.dto.response.RevenueBy10NearestDay
import com.ChickenKitchen.app.model.dto.response.StorePerformanceResponse
import com.ChickenKitchen.app.model.dto.response.UserGrowthResponse

interface DashboardService {

    // Doanh thu theo 10 ngày gần nhất
    fun getRevenueBy10NearestDays(): List<RevenueBy10NearestDay>

    // Hiệu suất cửa hàng (top store có doanh thu cao nhất)
    fun getTopStorePerformance(): StorePerformanceResponse

    // Tăng trưởng người dùng (6 tháng gần nhất)
    fun getUserGrowth(): UserGrowthResponse
}