package com.ChickenKitchen.app.model.entity.ingredient

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "ingredient_nutrients")
class IngredientNutrient(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    var ingredient: Ingredient,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrient_id", nullable = false)
    var nutrient: Nutrient,

    @Column(nullable = false)
    var amount: BigDecimal
)
