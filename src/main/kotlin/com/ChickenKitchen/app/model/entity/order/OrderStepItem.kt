package com.ChickenKitchen.app.model.entity.order

import com.ChickenKitchen.app.model.entity.menu.DailyMenuItem
import jakarta.persistence.*

@Entity
@Table(name = "order_step_items")
class OrderStepItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_step_id", nullable = false)
    val orderStep: OrderStep,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_menu_item_id", nullable = false)
    val dailyMenuItem: DailyMenuItem,

    @Column(nullable = false)
    val quantity: Int
)

