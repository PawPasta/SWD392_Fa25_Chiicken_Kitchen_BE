package com.ChickenKitchen.app.model.entity.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.entity.ingredient.Store
import com.ChickenKitchen.app.model.entity.payment.Payment
import com.ChickenKitchen.app.model.entity.promotion.OrderPromotion
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.model.entity.user.User
import jakarta.persistence.*
import java.sql.Timestamp


@Entity
@Table(
    name = "orders",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_store_id", columnList = "store_id"),
        Index(name = "idx_status", columnList = "status")
    ]
)
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,

    @Column(name = "total_price", nullable = false)
    val totalPrice: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus,

    @Column(name = "pickup_time", nullable = false)
    val pickupTime: Timestamp? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @OneToMany(mappedBy = "order")
    val dishes: MutableList<Dish> = mutableListOf(),

    @OneToMany(mappedBy = "order")
    val orderPromotions: MutableList<OrderPromotion> = mutableListOf(),

    @OneToMany(mappedBy = "order")
    val payments: MutableList<Payment> = mutableListOf(),

    @OneToMany(mappedBy = "order")
    val orderSteps: MutableList<OrderStep> = mutableListOf()
)
