package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.PaymentMethodDetailResponse
import com.ChickenKitchen.app.model.dto.response.PaymentMethodResponse
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod

fun PaymentMethod.toPaymentMethodResponse() = PaymentMethodResponse(
    id = this.id!!,
    name = this.name,
    description = this.description,
    isActive = this.isActive
)

fun PaymentMethod.toPaymentMethodDetailResponse() = PaymentMethodDetailResponse(
    id = this.id!!,
    name = this.name,
    description = this.description,
    isActive = this.isActive
)

fun List<PaymentMethod>.toPaymentMethodResponseList() = this.map { it.toPaymentMethodResponse() }

