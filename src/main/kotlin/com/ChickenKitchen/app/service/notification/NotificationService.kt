package com.ChickenKitchen.app.service.notification

import com.ChickenKitchen.app.model.dto.request.MultipleNotificationRequest
import com.ChickenKitchen.app.model.dto.request.SingleNotificationRequest
import com.ChickenKitchen.app.model.entity.user.User


interface NotificationService {

    fun sendToUser(req: SingleNotificationRequest)

    fun sendToAllUsers(req: MultipleNotificationRequest)

    fun sendToUsers(req: MultipleNotificationRequest, users: List<User>)

}