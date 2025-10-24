package com.ChickenKitchen.app.model.entity.user
import com.ChickenKitchen.app.model.entity.payment.Transaction
import org.hibernate.annotations.CreationTimestamp


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
    var user: User,

    @Column(nullable = false)
    var balance: Int = 0,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Timestamp? = null,

    @Column(name = "updated_at")
    var updatedAt: Timestamp? = null,

    @OneToMany(mappedBy = "wallet", cascade = [CascadeType.ALL], orphanRemoval = true)
    var transactions: MutableList<Transaction> = mutableListOf()
)

