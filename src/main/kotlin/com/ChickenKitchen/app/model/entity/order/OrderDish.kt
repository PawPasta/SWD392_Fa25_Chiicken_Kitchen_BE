package com.ChickenKitchen.app.model.entity.order

import com.ChickenKitchen.app.model.entity.step.Dish
import jakarta.persistence.*

@Entity
@Table(name = "order_dishes")
class OrderDish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    val dish: Dish,

    @Column(name = "quantity", nullable = false)
    var quantity: Int = 1
)

