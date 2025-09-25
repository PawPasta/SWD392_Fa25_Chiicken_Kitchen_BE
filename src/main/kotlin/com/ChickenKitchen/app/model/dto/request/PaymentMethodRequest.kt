package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.PaymentMethodType

// Request dùng cho admin để CRUD PaymentMethod
data class CreatePaymentMethodRequest(
    val name: PaymentMethodType,
    val description: String? = null,
    val isActive: Boolean = true
)

data class UpdatePaymentMethodRequest(
    val name: PaymentMethodType? = null,
    val description: String? = null,
    val isActive: Boolean? = null
)

