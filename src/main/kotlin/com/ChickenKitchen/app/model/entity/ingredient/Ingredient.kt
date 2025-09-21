package com.ChickenKitchen.app.model.entity.ingredient

import jakarta.persistence.*
import java.math.BigDecimal
import com.ChickenKitchen.app.enum.IngredientCategory
import com.ChickenKitchen.app.enum.UnitEnum
import com.ChickenKitchen.app.model.entity.recipe.RecipeIngredient

@Entity
@Table(name = "ingredients")
class Ingredient(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var baseUnit: UnitEnum, // ví dụ: g, ml, cái...

    @Column(nullable = false)
    var baseQuantity: Int, // ví dụ: 100g

    @Column(nullable = false, precision = 15, scale = 2)
    var basePrice: BigDecimal, // giá theo baseQuantity

    var image: String? = null,

    @Enumerated(EnumType.STRING)
    var category: IngredientCategory, // Enum: vegetable, meat, grain...

    @Column(nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "ingredient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var ingredient_nutrients: MutableList<IngredientNutrient> = mutableListOf(),

    @OneToMany(mappedBy = "ingredient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var recipe_ingredients: MutableList<RecipeIngredient> = mutableListOf(),
)
