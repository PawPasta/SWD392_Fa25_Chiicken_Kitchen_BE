package com.ChickenKitchen.app.model.entity.step

import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.order.OrderStep
import jakarta.persistence.*
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(name = "dishes")
class Dish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @Column(name = "price", nullable = false)
    var price: Int = 0,

    @Column(name = "cal", nullable = false)
    var cal: Int = 0,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: Timestamp? = null,

    @Column(name = "note")
    var note: String? = null,

    @OneToMany(mappedBy = "dish")
    val orderSteps: MutableList<OrderStep> = mutableListOf()
)
