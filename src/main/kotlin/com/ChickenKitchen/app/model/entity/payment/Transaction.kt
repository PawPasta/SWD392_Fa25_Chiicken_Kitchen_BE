package com.ChickenKitchen.app.model.entity.payment

import com.ChickenKitchen.app.enums.TransactionStatus
import jakarta.persistence.*
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(
    name = "transactions",
    indexes = [
        Index(name = "idx_payment_id", columnList = "payment_id"),
        Index(name = "idx_payment_method_id", columnList = "payment_method_id")
    ]
)
class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    val payment: Payment,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    val paymentMethod: PaymentMethod,

    // Align with ERD: store "transaction_type" while reusing existing enum
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    var transactionType: TransactionStatus,

    @Column(nullable = false)
    val amount: Int,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @Column(columnDefinition = "TEXT")
    val note: String? = null
)
