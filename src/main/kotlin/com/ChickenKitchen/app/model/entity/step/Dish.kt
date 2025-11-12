package com.ChickenKitchen.app.model.entity.step

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

    @Column(name = "name", nullable = false, length = 150)
    var name: String = "",

    @Column(name = "price", nullable = false)
    var price: Int = 0,

    @Column(name = "cal", nullable = false)
    var cal: Int = 0,

    @Column(name = "is_custom", nullable = false)
    var isCustom: Boolean = false,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: Timestamp? = null,

    @Column(name = "note")
    var note: String? = null,

    @Column(name = "image_url", length = 500)
    var imageUrl: String? = null,

    @OneToMany(mappedBy = "dish")
    val orderSteps: MutableList<OrderStep> = mutableListOf(),

    @Column(name = "nutrient_json", columnDefinition = "TEXT")
    var nutrientJson: String? = null
)
