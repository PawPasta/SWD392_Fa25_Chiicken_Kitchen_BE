package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.DiscountType
import java.math.BigDecimal
import java.sql.Timestamp

// Request dùng cho admin để CRUD Promotion
data class CreatePromotionRequest(
    val discountType: DiscountType,
    val discountValue: BigDecimal,
    val startDate: Timestamp,
    val endDate: Timestamp,
    val quantity: Int,
    val isActive: Boolean = true
)

data class UpdatePromotionRequest(
    val discountType: DiscountType? = null,
    val discountValue: BigDecimal? = null,
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null,
    val quantity: Int? = null,
    val isActive: Boolean? = null
)

