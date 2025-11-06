package com.ChickenKitchen.app.service.payment

import com.ChickenKitchen.app.model.dto.request.CreatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.response.PaymentMethodResponse
import com.ChickenKitchen.app.service.BaseService

interface PaymentMethodService : BaseService<PaymentMethodResponse, PaymentMethodResponse, CreatePaymentMethodRequest, UpdatePaymentMethodRequest, Long> {
    fun changeStatus (id: Long) : PaymentMethodResponse
    fun getAll(pageNumber: Int, size: Int): List<PaymentMethodResponse>?
    fun count(): Long
}
