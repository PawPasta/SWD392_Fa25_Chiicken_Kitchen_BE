package com.ChickenKitchen.app.service.dashboard

import com.ChickenKitchen.app.model.dto.response.OrderPickupRevenueProjection
import com.ChickenKitchen.app.model.dto.response.PopularDishResponse
import com.ChickenKitchen.app.model.dto.response.ReportSummaryResponse
import com.ChickenKitchen.app.model.dto.response.StoreBestPerformance
import com.ChickenKitchen.app.model.dto.response.UserGrowthProjection
import java.sql.Timestamp

interface DashboardService {

    fun getBestStorePerformance () : List<StoreBestPerformance>

    fun getRevenueTrend(dayNumber : Int) : List<OrderPickupRevenueProjection>

    fun getUserGrowthTrend(dayNumber: Int): List<UserGrowthProjection>

    fun getSummary(startDate: Timestamp, endDate: Timestamp): ReportSummaryResponse

    fun getPopularDishes(startDate: Timestamp, endDate: Timestamp, limit: Int): List<PopularDishResponse>

}