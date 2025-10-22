package com.ChickenKitchen.app.model.entity.promotion

import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

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
    val usedDate: LocalDateTime? = null,
)