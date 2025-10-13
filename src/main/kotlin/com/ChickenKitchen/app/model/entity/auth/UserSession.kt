package com.ChickenKitchen.app.model.entity.auth
import User
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.sql.Timestamp

@Entity
@Table(
    name = "user_sessions",
    indexes = [Index(name = "idx_user_id", columnList = "user_id")]
)
class UserSession(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "session_token")
    val sessionToken: String? = null,

    @Column(name = "refresh_token")
    val refreshToken: String? = null,

    @Column(name = "expires_at")
    val expiresAt: Timestamp? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "device_info", columnDefinition = "json")
    val deviceInfo: String? = null,

    @Column(name = "last_activity")
    var lastActivity: Timestamp? = null,

    @Column(name = "is_cancelled", nullable = false)
    var isCancelled: Boolean = false
)