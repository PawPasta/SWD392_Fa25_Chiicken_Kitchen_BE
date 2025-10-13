package com.ChickenKitchen.app.model.entity.category

import com.ChickenKitchen.app.model.entity.step.Step
import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column
    val description: String? = null,

    @OneToMany(mappedBy = "category")
    val steps: MutableList<Step> = mutableListOf()
)