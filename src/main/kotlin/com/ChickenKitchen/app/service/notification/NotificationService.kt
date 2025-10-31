package com.ChickenKitchen.app.service.notification

import com.ChickenKitchen.app.model.dto.request.MultipleNotificationRequest
import com.ChickenKitchen.app.model.dto.request.SingleNotificationRequest


interface NotificationService {

    fun sendToUser(req: SingleNotificationRequest)

    fun sendToAllUsers(req: MultipleNotificationRequest)

}