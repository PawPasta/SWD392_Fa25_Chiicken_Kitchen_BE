package com.Chicken_Kitchen.model.entity.user
import jakarta.persistence.*
import java.sql.Timestamp

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
    var expiredAt: Timestamp,

    var isCanceled: Boolean = false
)
