package com.ChickenKitchen.app.model.entity.user

import com.ChickenKitchen.app.enum.Role
import com.ChickenKitchen.app.model.entity.auth.MailToken
import com.ChickenKitchen.app.model.entity.auth.UserSession
import com.ChickenKitchen.app.model.entity.order.Order
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import java.time.LocalDate


@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_email", columnList = "email")
    ]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role,

    @Column(name = "uid", unique = true, length = 128)
    var uid: String? = null,

    @Column(nullable = false, unique = true, length = 100)
    var email: String,

    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false,

    @Column(length = 100)
    var phone: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    var createdAt: Timestamp? = null,

    @Column(name = "updated_at")
    @UpdateTimestamp
    var updatedAt: Timestamp? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = false,

    @Column(name = "full_name", nullable = false, length = 100)
    var fullName: String,

    @Column(name = "image", nullable = true, length = 100)
    var imageURL: String?,

    @Column(name = "birthday")
    var birthday: LocalDate? = null,

    @Column(name = "provider", nullable = true, length = 100)
    var provider: String?,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var sessions: MutableList<UserSession> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var mailTokens: MutableList<MailToken> = mutableListOf(),

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var wallet: Wallet? = null,

    @OneToMany(mappedBy = "user")
    var orders: MutableList<Order> = mutableListOf(),
)
