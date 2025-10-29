package com.ChickenKitchen.app.model.dto.response


// Nay` la tim kiem doanh thu theo 10 ngay gan nhat ne
data class RevenueBy10NearestDay(
    val date: String,
    val revenue: Double
)

// Nay` la tim kiem store co doanh thu cao nhat ne
data class StorePerformanceResponse(
    val totalRevenue: Double,
    val averageOrderValue: Double
)


//Nay la so luong tang truong nguoi dung ne (6 thang)
data class MonthlyUserGrowth(
    val month: String,
    val newUsers: Int
)

data class UserGrowthResponse(
    val totalNewUsers: Int,
    val monthlyGrowth: List<MonthlyUserGrowth>
)