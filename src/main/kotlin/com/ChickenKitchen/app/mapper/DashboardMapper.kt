package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.OrderPickupRevenueProjection
import com.ChickenKitchen.app.model.dto.response.StoreBestPerformance
import com.ChickenKitchen.app.model.dto.response.UserGrowthProjection
import com.ChickenKitchen.app.model.entity.ingredient.Store
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.user.User
import java.util.Date


fun Store.toStoreBestPerformance(): StoreBestPerformance =
    StoreBestPerformance(
        storeId = this.id!!,
        storeName = this.name,
        revenue = this.orders.sumOf { it.totalPrice.toLong() }
    )

fun List<Store>.toStoreBestPerformanceList(): List<StoreBestPerformance> =
    this.map { it.toStoreBestPerformance() }

fun Order.toRevenueTrendResponse(): OrderPickupRevenueProjection =
    OrderPickupRevenueProjection(
        pickupTime = this.pickupTime,
        totalPrice = this.totalPrice.toLong()
    )

fun List<Order>.toRevenueTrendResponseList(): List<OrderPickupRevenueProjection> =
    this.map { it.toRevenueTrendResponse() }

fun User.toUserGrowthResponse(): UserGrowthProjection =
    UserGrowthProjection(
        createdDate = Date(this.createdAt!!.time),
        totalUser = 1L
    )

fun List<User>.toUserGrowthResponseList(): List<UserGrowthProjection> =
    this.map { it.toUserGrowthResponse() }