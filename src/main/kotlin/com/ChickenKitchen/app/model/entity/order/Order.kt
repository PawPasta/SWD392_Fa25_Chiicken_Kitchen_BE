package com.ChickenKitchen.app.model.entity.order

import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.model.entity.user.UserAddress
import com.ChickenKitchen.app.model.entity.promotion.PromotionOrder
import com.ChickenKitchen.app.model.entity.transaction.Transaction
import com.ChickenKitchen.app.model.entity.order.Feedback
import jakarta.persistence.*
import java.sql.Timestamp
import com.ChickenKitchen.app.enum.OrderStatus
import java.math.BigDecimal

@Entity
@Table(name = "orders")
class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_address_id")
    var userAddress: UserAddress? = null,

    @Column(nullable = false)
    var totalPrice: BigDecimal = BigDecimal.ZERO,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus,   // Enum bạn định nghĩa

    @Column(nullable = false)
    var createdAt: Timestamp,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var order_status_history: MutableList<OrderStatusHistory> = mutableListOf(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var order_items: MutableList<OrderItem> = mutableListOf(),

    @OneToOne(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var feedbacks: Feedback? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var promotion_orders: MutableList<PromotionOrder> = mutableListOf(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var transactions: MutableList<Transaction> = mutableListOf(),
)
