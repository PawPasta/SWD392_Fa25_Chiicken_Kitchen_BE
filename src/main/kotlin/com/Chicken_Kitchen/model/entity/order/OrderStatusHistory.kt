package com.Chicken_Kitchen.model.entity.order

import jakarta.persistence.*
import java.time.LocalDate
import org.hibernate.annotations.CreationTimestamp
import com.Chicken_Kitchen.enum.OrderStatus

@Entity
@Table(name = "order_status_history")
class OrderStatusHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @Column(nullable = false)
    var status: OrderStatus,

    @CreationTimestamp
    @Column(nullable = false)
    var changeAt: LocalDate,

    @Column(nullable = false)
    var changeBy: LocalDate,

    var note: String? = null
)
