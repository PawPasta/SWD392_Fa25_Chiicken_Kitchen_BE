package com.ChickenKitchen.app.model.entity.payment

import User
import com.ChickenKitchen.app.enum.TransactionStatus
import com.ChickenKitchen.app.model.entity.order.Order
import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(
    name = "transactions",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_order_id", columnList = "order_id")
    ]
)
class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    val paymentMethod: PaymentMethod,

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    var transactionStatus: TransactionStatus,

    @Column(nullable = false)
    val amount: Int,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @Column(columnDefinition = "TEXT")
    val note: String? = null
)
