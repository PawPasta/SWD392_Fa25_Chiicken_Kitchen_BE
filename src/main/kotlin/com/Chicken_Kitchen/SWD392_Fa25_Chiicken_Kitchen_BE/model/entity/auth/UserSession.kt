package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.user
import jakarta.persistence.*
import java.time.Instant
import java.sql.Timestamp

@Entity 
@Table(name = "user_sessions")
class UserSession(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false, unique = true, length = 512)
    var sessionToken: String,

    @Column(nullable = false, unique = true, length = 512)
    var refreshToken: String,

    @Column(nullable = false)
    var expiresAt: Timestamp,

    @Column(columnDefinition = "JSON")
    var deviceInfo: String? = null,

    var lastActivity: Timestamp,
    var isCanceled: Boolean = false
)