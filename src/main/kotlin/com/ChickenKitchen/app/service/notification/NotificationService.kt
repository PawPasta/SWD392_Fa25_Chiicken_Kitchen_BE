package com.ChickenKitchen.app.service.notification

import com.ChickenKitchen.app.model.dto.request.MultipleNotificationRequest
import com.ChickenKitchen.app.model.dto.request.NotificationRequest


interface NotificationService {

    fun sendToToken(req : NotificationRequest)

    fun sendToMultipleTokens(req: MultipleNotificationRequest)

}