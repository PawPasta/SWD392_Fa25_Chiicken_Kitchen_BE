package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.PaymentMethodType

data class PaymentMethodResponse(
    val id: Long,
    val name: PaymentMethodType,
    val description: String?,
    val isActive: Boolean
)

data class PaymentMethodDetailResponse(
    val id: Long,
    val name: PaymentMethodType,
    val description: String?,
    val isActive: Boolean
)

