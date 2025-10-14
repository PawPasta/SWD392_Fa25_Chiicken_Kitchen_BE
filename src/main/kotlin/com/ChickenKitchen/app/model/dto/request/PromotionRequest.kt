package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.DiscountType
import java.time.LocalDateTime


data class CreatePromotionRequest (
    val discountType: DiscountType,
    val discountValue: Int,
    val startDate : LocalDateTime,
    val endDate: LocalDateTime,
    val isActive : Boolean = true,
    val quantity : Int,
)


data class UpdatePromotionRequest (
    val discountValue: Int?,
    val endDate: LocalDateTime?,
    val isActive: Boolean?,
    val quantity: Int?,
)