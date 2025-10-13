package com.ChickenKitchen.app.model.entity.step

import com.ChickenKitchen.app.model.entity.category.Category
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

    @OneToMany(mappedBy = "step")
    val stepItems: MutableList<StepItem> = mutableListOf()
)