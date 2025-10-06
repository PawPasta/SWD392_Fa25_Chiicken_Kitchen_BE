package com.ChickenKitchen.app.service.payment

import com.ChickenKitchen.app.enum.PaymentMethodType
import java.math.BigDecimal
import java.math.RoundingMode


interface PaymentService {

    fun convertUsdToVnd(usdAmount: BigDecimal, exchangeRate: BigDecimal = BigDecimal("24000")): Long {
        return usdAmount.multiply(exchangeRate)
            .setScale(0, RoundingMode.HALF_UP) // làm tròn đến số nguyên
            .longValueExact()
    }

    fun createURLService (orderId : Long, paymentMethod: PaymentMethodType) : String
    fun callBack (params: Map<String,String>) : String
}