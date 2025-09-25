package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.PromotionDetailResponse
import com.ChickenKitchen.app.model.dto.response.PromotionResponse
import com.ChickenKitchen.app.model.entity.promotion.Promotion

fun Promotion.toPromotionResponse() = PromotionResponse(
    id = this.id!!,
    name = this.name,
    description = this.description,
    discountType = this.discountType,
    discountValue = this.discountValue,
    isActive = this.isActive,
    startDate = this.startDate,
    endDate = this.endDate,
    quantity = this.quantity
)

fun Promotion.toPromotionDetailResponse() = PromotionDetailResponse(
    id = this.id!!,
    name = this.name,
    description = this.description,
    discountType = this.discountType,
    discountValue = this.discountValue,
    isActive = this.isActive,
    startDate = this.startDate,
    endDate = this.endDate,
    quantity = this.quantity
)

fun List<Promotion>.toPromotionResponseList() = this.map { it.toPromotionResponse() }
