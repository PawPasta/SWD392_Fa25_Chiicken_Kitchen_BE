package com.ChickenKitchen.app.serviceImpl.notification

import com.ChickenKitchen.app.model.dto.request.MultipleNotificationRequest
import com.ChickenKitchen.app.model.dto.request.NotificationRequest
import com.ChickenKitchen.app.service.notification.NotificationService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class NotificationServiceImpl : NotificationService {

    private val imageUrl = "https://tse3.mm.bing.net/th/id/OIP.Iuj2Lwxu4EIvK11euDVSPgHaEK?rs=1&pid=ImgDetMain&o=7&rm=3"

    override fun sendToToken(req: NotificationRequest) {
        val message = buildMessage(req.token, req.title, req.body)
        try {
            FirebaseMessaging.getInstance().send(message)
        } catch (_: Exception) { }
    }

    @Async
    override fun sendToMultipleTokens(req: MultipleNotificationRequest) {
        val tokens = req.tokens.filter { it.isNotBlank() }
        if (tokens.isEmpty()) return

        tokens.chunked(500).forEach { batch ->
            val messages = batch.map { token ->
                buildMessage(token, req.title, req.body)
            }
            try {
                FirebaseMessaging.getInstance().sendEach(messages)
            } catch (e: Exception) {

            }
        }
    }


    private fun buildMessage(token: String, title: String, body: String): Message {
        return Message.builder()
            .setToken(token)
            .putData("title", title)
            .putData("body", body)
            .putData("click_action", "FLUTTER_NOTIFICATION_CLICK") // để onMessageOpenedApp hoạt động
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .setImage(imageUrl)
                    .build()
            )
            .build()
    }

}