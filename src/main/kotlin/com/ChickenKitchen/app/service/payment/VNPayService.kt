package com.ChickenKitchen.app.service.payment


interface VNPayService {
    fun createVnPayURL (orderId: Long) : String
    fun callbackURL (params: Map<String, String>): String
}