package com.ChickenKitchen.app.service.payment

import com.ChickenKitchen.app.model.entity.order.Order


interface VNPayService {
    fun createVnPayURL (order : Order) : String
    fun callbackURL (params: Map<String, String>): String
}