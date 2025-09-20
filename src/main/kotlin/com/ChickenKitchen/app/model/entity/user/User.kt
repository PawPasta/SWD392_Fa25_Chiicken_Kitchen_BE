package com.ChickenKitchen.app.model.entity.user

import com.ChickenKitchen.app.enum.Role
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.order.Feedback
import com.ChickenKitchen.app.model.entity.promotion.PromotionOrder
import com.ChickenKitchen.app.model.entity.transaction.Transaction
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate
import java.sql.Timestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.CreationTimestamp
import com.ChickenKitchen.app.model.entity.auth.UserSession
import com.ChickenKitchen.app.model.entity.auth.MailToken

@Entity 
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER,

    @Column(nullable = false, unique = true)
    var username: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String,

    var isVerify: Boolean = false,

    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: Timestamp? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Timestamp? = null,

    var isActive: Boolean = true,

    var firstName: String? = null,
    var lastName: String? = null,
    var birthday: LocalDate? = null,

    /** JSON sở thích / dị ứng… */
    @Column(columnDefinition = "json")
    var preferences: String? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var sessions: MutableList<UserSession> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var mail_tokens: MutableList<MailToken> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var user_adresss: MutableList<UserAddress> = mutableListOf(),

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var wallet: Wallet? = null,    

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var orders: MutableList<Order> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var feedbacks: MutableList<Feedback> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var promotion_orders: MutableList<PromotionOrder> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var transactions: MutableList<Transaction> = mutableListOf(),
)