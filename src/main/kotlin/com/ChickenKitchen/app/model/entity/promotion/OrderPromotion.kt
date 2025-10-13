package com.ChickenKitchen.app.model.entity.promotion

import User
import com.ChickenKitchen.app.model.entity.order.Order
import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "order_promotions")
class OrderPromotion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    val promotion: Promotion,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "used_date", nullable = false)
    val usedDate: Timestamp? = null,
)