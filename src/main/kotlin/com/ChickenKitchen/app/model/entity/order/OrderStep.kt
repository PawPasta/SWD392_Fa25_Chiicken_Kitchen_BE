package com.ChickenKitchen.app.model.entity.order

import com.ChickenKitchen.app.model.entity.step.Step
import com.ChickenKitchen.app.model.entity.step.Dish
import jakarta.persistence.*

@Entity
@Table(name = "order_steps")
class OrderStep(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    val dish: Dish,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    val step: Step,

    @OneToMany(mappedBy = "orderStep", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<OrderStepItem> = mutableListOf()
)
