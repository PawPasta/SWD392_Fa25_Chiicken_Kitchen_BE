package com.ChickenKitchen.app.model.entity.recipe

import jakarta.persistence.*
import java.math.BigDecimal
import com.ChickenKitchen.app.enum.UnitEnum
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.model.entity.cooking.CookingMethod

@Entity
@Table(name = "recipe_ingredients")
class RecipeIngredient(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    var recipe: Recipe,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    var ingredient: Ingredient,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooking_method_id", nullable = false)
    var cookingMethod: CookingMethod,

    @Column(nullable = false)
    var quantity: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var baseUnit: UnitEnum, // Enum: g, ml, piece...

    @Column(nullable = false, precision = 15, scale = 2)
    var price: BigDecimal,

    @Column(nullable = false)
    var cal: Int,

    @Column(columnDefinition = "JSON")
    var itemSnapshot: String? = null
)
