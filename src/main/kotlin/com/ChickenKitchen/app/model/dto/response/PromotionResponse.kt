package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enums.DiscountType
import java.time.LocalDateTime


data class PromotionResponse (
    val id : Long,
    val discountType: DiscountType,
    val discountValue: Int,
    val startDate : LocalDateTime,
    val endDate : LocalDateTime,
    val isActive : Boolean,
)

data class PromotionDetailResponse (
    val id : Long,
    val discountType: DiscountType,
    val discountValue: Int,
    val startDate : LocalDateTime,
    val endDate : LocalDateTime,
    val quantity: Int,
    val isActive : Boolean,
    val orderPromotionResponse: List<OrderPromotionResponse>
)

data class OrderPromotionResponse (
    val id : Long,
    val usedDate: LocalDateTime?
)