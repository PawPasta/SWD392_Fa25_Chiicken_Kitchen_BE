package com.ChickenKitchen.app.model.entity.ingredient

import jakarta.persistence.*
import com.ChickenKitchen.app.enum.UnitEnum

@Entity
@Table(name = "nutrients")
class Nutrient(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var baseUnit: UnitEnum,   // ví dụ: g, mg, kcal

    @OneToMany(mappedBy = "nutrient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var ingredient_nutrients: MutableList<IngredientNutrient> = mutableListOf(),
)
