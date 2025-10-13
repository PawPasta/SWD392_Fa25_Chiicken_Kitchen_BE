package com.ChickenKitchen.app.model.entity.ingredient

import jakarta.persistence.*

@Entity
@Table(name = "store_ingredient_batches")
class StoreIngredientBatch(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    val ingredient: Ingredient,

    @Column(nullable = false)
    var quantity: Long
)