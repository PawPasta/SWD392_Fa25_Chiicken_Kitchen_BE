package com.ChickenKitchen.app.model.entity.order

import com.ChickenKitchen.app.model.entity.ingredient.Store
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
@Table(
    name = "feedbacks",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["order_id"]) // one feedback per order
    ],
    indexes = [
        Index(name = "idx_feedback_store_id", columnList = "store_id"),
        Index(name = "idx_feedback_order_id", columnList = "order_id")
    ]
)
class Feedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    val order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,

    @Column(name = "rating", nullable = false)
    var rating: Int,

    @Column(name = "message", columnDefinition = "TEXT")
    var message: String? = null,

    @Column(name = "reply", columnDefinition = "TEXT")
    var reply: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: Timestamp? = null,
)

