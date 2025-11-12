package com.ChickenKitchen.app.serviceImpl.notification

import com.ChickenKitchen.app.model.dto.request.MultipleNotificationRequest
import com.ChickenKitchen.app.model.dto.request.SingleNotificationRequest
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.notification.NotificationService
import com.ChickenKitchen.app.util.EmailUtil
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class NotificationServiceImpl(
    private val userRepository: UserRepository,

    private val emailUtil: EmailUtil
) : NotificationService {

    private val imageUrl = "https://i.pinimg.com/1200x/1c/dd/c0/1cddc02da4fd7350fbf020764e5e612c.jpg"

    override fun sendToUser(req: SingleNotificationRequest) {
        emailUtil.send(req.user.email, req.title, req.body)
        val fcmToken = req.user.fcmToken?.takeIf { it.isNotBlank() } ?: return
        try {
            FirebaseMessaging.getInstance().send(
                buildMessage(fcmToken, req.title, req.body)
            )

        } catch (_: Exception) { }
    }

    @Async
    override fun sendToAllUsers(req: MultipleNotificationRequest) {
        val tokens = userRepository.findAll()
            .mapNotNull { it.fcmToken }
            .filter { it.isNotBlank() }

        if (tokens.isEmpty()) return

        tokens.chunked(500).forEach { batch ->
            val messages = batch.map { token -> buildMessage(token, req.title, req.body) }
            try {
                FirebaseMessaging.getInstance().sendEach(messages)
            } catch (_: Exception) { }
        }
    }

    @Async
    override fun sendToUsers(req: MultipleNotificationRequest, users: List<User>) {
        val tokens = users.mapNotNull { it.fcmToken }.filter { it.isNotBlank() }

        if (tokens.isEmpty()) return

        tokens.chunked(500).forEach { batch ->
            val messages = batch.map { token -> buildMessage(token, req.title, req.body) }
            try {
                FirebaseMessaging.getInstance().sendEach(messages)
            } catch (_: Exception) { }
        }
    }

    private fun buildMessage(token: String, title: String, body: String): Message {
        return Message.builder()
            .setToken(token)
            .putData("title", title)
            .putData("body", body)
            .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
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
