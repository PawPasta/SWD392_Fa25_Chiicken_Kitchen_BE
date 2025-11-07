package com.ChickenKitchen.app.model.dto.response


import java.sql.Timestamp
import java.util.Date

data class StoreBestPerformance(
    val storeId : Long,
    val storeName : String,
    val revenue : Long,
)

data class OrderPickupRevenueProjection(
    val pickupTime: Timestamp?,
    val totalPrice: Long?
)

data class UserGrowthProjection(
    val createdDate: Date,
    val totalUser: Long
)

data class ReportSummaryResponse(
    val totalRevenue: Long,
    val totalOrders: Long,
    val totalCustomers: Long,
    val averageOrderValue: Long
)


data class PopularDishResponse(
    val dishId: Long,
    val name: String,
    val quantitySold: Long,
    val revenue: Long,
    val category: String
)
