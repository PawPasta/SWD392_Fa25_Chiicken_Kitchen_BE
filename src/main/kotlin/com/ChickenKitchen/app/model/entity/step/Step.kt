package com.ChickenKitchen.app.model.entity.step

import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.model.entity.order.OrderStep
import jakarta.persistence.*

@Entity
@Table(name = "steps")
class Step(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,

    @Column(nullable = false)
    val name: String,

    @Column
    val description: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "step_number", nullable = false)
    val stepNumber: Int,

    @OneToMany(mappedBy = "step")
    val orderSteps: MutableList<OrderStep> = mutableListOf()
    
)