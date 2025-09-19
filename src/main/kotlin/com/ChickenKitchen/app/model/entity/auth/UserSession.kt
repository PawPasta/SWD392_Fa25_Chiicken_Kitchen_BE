package com.ChickenKitchen.app.model.entity.auth
import jakarta.persistence.*
import java.time.Instant
import java.sql.Timestamp
import com.ChickenKitchen.app.model.entity.user.User

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
    var expiredAt: Timestamp,

    @Column(columnDefinition = "JSON")
    var deviceInfo: String? = null,

    var lastActivity: Timestamp,
    var isCanceled: Boolean = false
)