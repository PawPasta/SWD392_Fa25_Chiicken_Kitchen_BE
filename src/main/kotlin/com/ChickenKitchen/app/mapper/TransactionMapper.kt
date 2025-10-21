package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.PaymentMethodResponse
import com.ChickenKitchen.app.model.dto.response.TransactionResponse
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.model.entity.payment.Transaction

//Cai nay de cho quan ly payment method
fun PaymentMethod.toPaymentMethodResponse(): PaymentMethodResponse =
    PaymentMethodResponse (
        id = this.id!!,
        description = this.description,
        isActive = this.isActive,
        name = this.name
    )

fun List<PaymentMethod>.toListPaymentMethodResponse() : List<PaymentMethodResponse> =
    this.map { it.toPaymentMethodResponse() }


// Cai nay cho Transaction ne

fun Transaction.toTransactionResponse() : TransactionResponse =
    TransactionResponse (
        id = this.id!!,
        amount = this.amount,
        createAt = this.createdAt,
        note = this.note,
        paymentId = this.payment.id,
        paymentMethodId = this.paymentMethod.id,
        transactionType = this.transactionType,
    )

fun List<Transaction>.toListTransactionResponse() : List<TransactionResponse> =
    this.map {it.toTransactionResponse()}
