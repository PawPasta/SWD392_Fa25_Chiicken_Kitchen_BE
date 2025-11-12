package com.ChickenKitchen.app.model.entity.ai

import com.ChickenKitchen.app.model.entity.auth.UserSession
import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "chat_messages", indexes = [Index(name = "idx_user_session_id", columnList = "user_session_id")])
class ChatMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_session_id", nullable = false)
    var userSession: UserSession,

    @Column(name = "role", length = 20, nullable = false)
    var role: String,

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var createdAt: Timestamp? = null
) {
    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = Timestamp(System.currentTimeMillis())
        }
    }
}

