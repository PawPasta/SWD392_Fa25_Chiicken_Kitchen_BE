package com.ChickenKitchen.app.model.entity.step

import com.ChickenKitchen.app.model.entity.menu.MenuItem
import jakarta.persistence.*

@Entity
@Table(name = "step_items")
class StepItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    val step: Step,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    val menuItem: MenuItem
)