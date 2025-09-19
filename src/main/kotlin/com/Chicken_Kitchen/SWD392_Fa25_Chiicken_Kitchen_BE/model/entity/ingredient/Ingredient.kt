package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.ingredient

import jakarta.persistence.*
import java.math.BigDecimal
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.enum.IngredientCategory
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.recipe.RecipeIngredient

@Entity
@Table(name = "ingredients")
class Ingredient(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var baseUnit: String, // ví dụ: g, ml, cái...

    @Column(nullable = false)
    var baseQuantity: Int, // ví dụ: 100g

    @Column(nullable = false, precision = 15, scale = 2)
    var basePrice: BigDecimal, // giá theo baseQuantity

    var image: String? = null,

    @Enumerated(EnumType.STRING)
    var category: IngredientCategory? = null, // Enum: vegetable, meat, grain...

    @Column(nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "ingredient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var ingredient_nutrients: MutableList<IngredientNutrient> = mutableListOf(),

    @OneToMany(mappedBy = "ingredient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var recipe_ingredients: MutableList<RecipeIngredient> = mutableListOf(),
)
