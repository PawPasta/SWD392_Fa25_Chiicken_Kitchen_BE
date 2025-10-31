package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.model.entity.user.User

data class SingleNotificationRequest(
    val user: User,
    val title: String,
    val body: String
)

data class MultipleNotificationRequest(
    val title: String,
    val body: String
)