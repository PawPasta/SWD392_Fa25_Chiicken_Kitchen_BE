package com.Chicken_Kitchen.model.entity.promotion

import com.Chicken_Kitchen.model.entity.order.Order
import com.Chicken_Kitchen.model.entity.user.User
import jakarta.persistence.*
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "promotion_orders")
class PromotionOrder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    var promotion: Promotion,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @CreationTimestamp
    @Column(nullable = false)
    var usedDate: Timestamp
)
