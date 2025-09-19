package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.ingredient

import jakarta.persistence.*

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
    var amount: Int
)
