package com.ChickenKitchen.app.model.dto.request

data class NotificationRequest(
    val token: String,
    val title: String,
    val body: String
)

data class MultipleNotificationRequest(
    val tokens: List<String>,
    val title: String,
    val body: String
)