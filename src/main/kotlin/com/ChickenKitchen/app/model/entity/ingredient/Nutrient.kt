package com.ChickenKitchen.app.model.entity.ingredient

import jakarta.persistence.*
import com.ChickenKitchen.app.enum.UnitEnum
import com.ChickenKitchen.app.model.entity.cooking.CookingEffect

@Entity
@Table(name = "nutrients")
class Nutrient(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var baseUnit: UnitEnum,   // ví dụ: g, mg, kcal

    @OneToMany(mappedBy = "nutrient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var ingredient_nutrients: MutableList<IngredientNutrient> = mutableListOf(),

    @OneToMany(mappedBy = "nutrient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var cooking_effects: MutableList<CookingEffect> = mutableListOf(),
)
