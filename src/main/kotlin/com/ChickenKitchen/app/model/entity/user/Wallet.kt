package com.ChickenKitchen.app.model.entity.user

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "wallets")
class Wallet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: User,

    @Column(nullable = false)
    var balance: Int = 0,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @Column(name = "updated_at")
    var updatedAt: Timestamp? = null,
)

