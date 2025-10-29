package com.ChickenKitchen.app.service.payment

import com.ChickenKitchen.app.model.dto.response.TransactionResponse
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.payment.Payment
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod


interface TransactionService {

    fun getAll() : List<TransactionResponse>?
    fun getAll(pageNumber: Int, size: Int): List<TransactionResponse>?
    fun getById(id : Long) : TransactionResponse
    fun createPaymentTransaction(payment: Payment, order: Order, paymentMethod: PaymentMethod ) : String
    fun count(): Long
}
