package com.ChickenKitchen.app.model.entity.ingredient

import jakarta.persistence.*
import java.math.BigDecimal
import com.ChickenKitchen.app.enum.UnitEnum
import com.ChickenKitchen.app.model.entity.recipe.RecipeIngredient
import com.ChickenKitchen.app.model.entity.category.Category

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
    var price: BigDecimal, // giá theo baseQuantity

    @Column(nullable = false)
    var cal: Int, // calories tương ứng với baseQuantity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category,

    var image: String? = null,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "ingredient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var ingredient_nutrients: MutableList<IngredientNutrient> = mutableListOf(),

    @OneToMany(mappedBy = "ingredient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var recipe_ingredients: MutableList<RecipeIngredient> = mutableListOf(),

)
