package com.ChickenKitchen.app.model.entity.order

import com.ChickenKitchen.app.model.entity.step.Step
import com.ChickenKitchen.app.model.entity.menu.MenuItem
import com.ChickenKitchen.app.model.entity.order.Order
import jakarta.persistence.*

@Entity
@Table(name = "order_steps")
class OrderStep(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    val step: Step,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    val menuItem: MenuItem,

    @Column(nullable = false)
    val quantity: Int
)
