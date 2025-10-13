package com.ChickenKitchen.app.model.entity.auth

import User
import jakarta.persistence.*
import java.sql.Timestamp


@Entity
@Table(
    name = "mail_tokens",
    indexes = [Index(name = "idx_user_id", columnList = "user_id")]
)
class MailToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "token")
    val token: String? = null,

    @Column(name = "expired_at")
    val expiredAt: Timestamp? = null,

    @Column(name = "is_cancelled", nullable = false)
    var isCancelled: Boolean = false
)