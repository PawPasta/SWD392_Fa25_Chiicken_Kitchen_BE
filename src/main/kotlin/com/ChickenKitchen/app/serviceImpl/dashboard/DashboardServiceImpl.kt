package com.ChickenKitchen.app.serviceImpl.dashboard

import com.ChickenKitchen.app.mapper.toStoreBestPerformanceList
import com.ChickenKitchen.app.model.dto.response.OrderPickupRevenueProjection
import com.ChickenKitchen.app.model.dto.response.PopularDishResponse
import com.ChickenKitchen.app.model.dto.response.ReportSummaryResponse
import com.ChickenKitchen.app.model.dto.response.StoreBestPerformance
import com.ChickenKitchen.app.model.dto.response.UserGrowthProjection
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.dashboard.DashboardService
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class DashboardServiceImpl(
    private val storeRepository: StoreRepository,
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val dishRepository: DishRepository,
) : DashboardService{

    override fun getBestStorePerformance() : List<StoreBestPerformance>{
        val store = storeRepository.findStoreWithHighestTotalPrice()
        return  store.toStoreBestPerformanceList()
    }

    override fun getRevenueTrend(dayNumber: Int): List<OrderPickupRevenueProjection> {

        val fromDate = Timestamp.valueOf(LocalDateTime.now().minusDays(dayNumber.toLong()))

        return orderRepository.findPickupAndTotalSince(fromDate)
    }

    override fun getUserGrowthTrend(dayNumber: Int): List<UserGrowthProjection> {

        val formDate = Timestamp.valueOf(LocalDateTime.now().minusDays(dayNumber.toLong()))
        return userRepository.countUsersGroupedByDateSince(formDate)
    }

    override fun getSummary(
        startDate: Timestamp,
        endDate: Timestamp
    ): ReportSummaryResponse {
        val totalRevenue = orderRepository.getTotalRevenue(startDate, endDate) ?: 0
        val totalOrders = orderRepository.getTotalOrders(startDate, endDate) ?: 0
        val totalCustomers = orderRepository.getTotalCustomers(startDate, endDate) ?: 0
        val avgOrderValue = if (totalOrders > 0) totalRevenue / totalOrders else 0

        return ReportSummaryResponse(
            totalRevenue = totalRevenue,
            totalOrders = totalOrders,
            totalCustomers = totalCustomers,
            averageOrderValue = avgOrderValue
        )
    }

    override fun getPopularDishes(
        startDate: Timestamp,
        endDate: Timestamp,
        limit: Int
    ): List<PopularDishResponse> {
        return dishRepository.findPopularDishes(startDate, endDate).take(limit)
    }

}