package com.ChickenKitchen.app.model.entity.step
import com.ChickenKitchen.app.model.entity.order.Order
import jakarta.persistence.*

@Entity
@Table(name = "dishes")
class Dish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @Column
    val name: String? = null,

    @Column(name = "is_customizable", nullable = false)
    val isCustomizable: Boolean = false,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true
)