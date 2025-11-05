package com.ChickenKitchen.app.service.payment

import com.ChickenKitchen.app.model.entity.order.Order


interface MomoService {

    fun createMomoURL(order: Order, channel : String?): String
    fun callBack(params: Map<String, Any>): String
    fun createMomoURLTest(amount: Long): String
}
