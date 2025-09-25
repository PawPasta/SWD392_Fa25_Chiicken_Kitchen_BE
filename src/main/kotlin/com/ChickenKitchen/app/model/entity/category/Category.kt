package com.ChickenKitchen.app.model.entity.category

import jakarta.persistence.*
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient

@Entity
@Table(name = "categories")
class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    var description: String? = null,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var ingredient: MutableList<Ingredient> = mutableListOf(),
)