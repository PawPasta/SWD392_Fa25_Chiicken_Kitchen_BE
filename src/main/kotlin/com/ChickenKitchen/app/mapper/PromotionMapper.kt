package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.OrderPromotionResponse
import com.ChickenKitchen.app.model.dto.response.PromotionDetailResponse
import com.ChickenKitchen.app.model.dto.response.PromotionResponse
import com.ChickenKitchen.app.model.entity.promotion.OrderPromotion
import com.ChickenKitchen.app.model.entity.promotion.Promotion

fun Promotion.toPromotionResponse () : PromotionResponse =
    PromotionResponse (
        id = this.id!!,
        name = this.name,
        description = this.description,
        code = this.code,
        discountType = this.discountType,
        discountValue = this.discountValue,
        endDate = this.endDate,
        startDate = this.startDate,
        isActive = this.isActive
    )

fun List<Promotion>.toPromotionList() : List<PromotionResponse> =
    this.map {it.toPromotionResponse()}


fun OrderPromotion.toOrderPromotionResponse() : OrderPromotionResponse =
    OrderPromotionResponse(
        id = this.id!!,
        usedDate = this.usedDate
    )
fun Promotion.toPromotionDetailResponse() : PromotionDetailResponse =
    PromotionDetailResponse (
        id = this.id!!,
        name = this.name,
        description = this.description,
        code = this.code,
        discountType = this.discountType,
        discountValue = this.discountValue,
        endDate = this.endDate,
        startDate = this.startDate,
        isActive = this.isActive,
        orderPromotionResponse = this.orderPromotions.map { it.toOrderPromotionResponse() } ,
        quantity = this.quantity
    )