package com.ChickenKitchen.app.service.payment

import com.ChickenKitchen.app.model.dto.request.CreatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.response.PaymentMethodDetailResponse
import com.ChickenKitchen.app.model.dto.response.PaymentMethodResponse
import com.ChickenKitchen.app.service.BaseService

interface PaymentMethodService : BaseService<PaymentMethodResponse, PaymentMethodDetailResponse,
        CreatePaymentMethodRequest, UpdatePaymentMethodRequest, Long> {
    fun changeStatus(id: Long): PaymentMethodResponse
}

