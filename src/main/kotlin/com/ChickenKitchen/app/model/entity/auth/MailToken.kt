package com.ChickenKitchen.app.model.entity.auth
import jakarta.persistence.*
import java.sql.Timestamp
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.enum.MailType

@Entity 
@Table(name = "mail_tokens")
class MailToken(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(nullable = false, unique = true, length = 512)
    var token: String,

    @Column(nullable = false)
    val type: MailType,

    @Column(nullable = false)
    var expiredAt: Timestamp,

    var isCanceled: Boolean = false
)
