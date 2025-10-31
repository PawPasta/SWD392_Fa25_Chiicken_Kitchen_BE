package com.ChickenKitchen.app.model.entity.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.entity.ingredient.Store
import com.ChickenKitchen.app.model.entity.payment.Payment
import com.ChickenKitchen.app.model.entity.promotion.OrderPromotion
import com.ChickenKitchen.app.model.entity.order.OrderDish
import com.ChickenKitchen.app.model.entity.user.User
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
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
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    var store: Store,

    @Column(name = "total_price", nullable = false)
    var totalPrice: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus,

    @Column(name = "pickup_time", nullable = false)
    var pickupTime: Timestamp? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Timestamp? = null,

    // Dishes are linked via OrderDish join entity (order_dishes table)
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var orderDishes: MutableList<OrderDish> = mutableListOf(),

    @OneToMany(mappedBy = "order")
    var orderPromotions: MutableList<OrderPromotion> = mutableListOf(),

    @OneToOne(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val payment: Payment? = null,

    @OneToOne(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var feedback: Feedback? = null

    // Order steps now link via Dish -> OrderStep (dish_id),
    // so we remove direct Order -> OrderStep mapping.
)
