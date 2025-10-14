package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.PaymentMethodResponse
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod


fun PaymentMethod.toPaymentMethodResponse(): PaymentMethodResponse =
    PaymentMethodResponse (
        id = this.id!!,
        description = this.description,
        isActive = this.isActive,
        name = this.name
    )

fun List<PaymentMethod>.toListPaymentMethodResponse() : List<PaymentMethodResponse> =
    this.map { it.toPaymentMethodResponse() }