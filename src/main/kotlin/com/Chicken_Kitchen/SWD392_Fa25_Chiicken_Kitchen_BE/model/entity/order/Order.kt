package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.order

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.user.User
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.promotion.PromotionOrder
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.transaction.Transaction
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.order.Feedback
import jakarta.persistence.*
import java.sql.Timestamp
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.enum.OrderStatus
import java.math.BigDecimal

@Entity
@Table(name = "orders")
class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

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

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var order_combos: MutableList<OrderCombo> = mutableListOf(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var feedbacks: MutableList<Feedback> = mutableListOf(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var promotion_orders: MutableList<PromotionOrder> = mutableListOf(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var transactions: MutableList<Transaction> = mutableListOf(),
)
