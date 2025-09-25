package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.DiscountType
import java.math.BigDecimal
import java.sql.Timestamp

data class PromotionResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val discountType: DiscountType,
    val discountValue: BigDecimal,
    val isActive: Boolean,
    val startDate: Timestamp,
    val endDate: Timestamp,
    val quantity: Int
)

data class PromotionDetailResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val discountType: DiscountType,
    val discountValue: BigDecimal,
    val isActive: Boolean,
    val startDate: Timestamp,
    val endDate: Timestamp,
    val quantity: Int
)
