package com.ChickenKitchen.app.model.entity.payment

import com.ChickenKitchen.app.enums.PaymentStatus
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.user.User
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp

@Entity
@Table(
    name = "payments",
    indexes = [
        Index(name = "idx_payment_user_id", columnList = "user_id"),
        Index(name = "idx_payment_order_id", columnList = "order_id"),
        Index(name = "idx_payment_status", columnList = "status")
    ]
)
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    var order: Order,

    @Column(nullable = false)
    var discountAmount: Int,

    @Column(nullable = false)
    var amount: Int,

    @Column(nullable = false)
    var finalAmount: Int,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    var status: PaymentStatus,

    @Column(columnDefinition = "TEXT")
    var note: String? = null,

    @OneToMany(mappedBy = "payment")
    val transactions: MutableList<Transaction> = mutableListOf()
)
