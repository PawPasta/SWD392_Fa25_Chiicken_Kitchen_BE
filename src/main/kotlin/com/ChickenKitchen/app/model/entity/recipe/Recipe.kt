package com.ChickenKitchen.app.model.entity.recipe

import jakarta.persistence.*
import java.math.BigDecimal
import com.ChickenKitchen.app.enum.RecipeCategory
import com.ChickenKitchen.app.model.entity.order.OrderItem
import com.ChickenKitchen.app.model.entity.combo.ComboItem

@Entity
@Table(name = "recipes")
class Recipe(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Lob
    var description: String? = null,

    @Column(nullable = false)
    var isCustomizable: Boolean = true,

    @Column(nullable = false, precision = 15, scale = 2)
    var basePrice: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var baseCal: Int,

    @Column(columnDefinition = "JSON")
    var ingredientSnapshot: String? = null,

    @Column(nullable = false)
    var isActive: Boolean = true,

    var image: String? = null,

    @Enumerated(EnumType.STRING)
    var category: RecipeCategory? = null,   // Enum bạn định nghĩa

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var order_items: MutableList<OrderItem> = mutableListOf(),

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var recipe_ingredients: MutableList<RecipeIngredient> = mutableListOf(),

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var combo_items: MutableList<ComboItem> = mutableListOf(),

)
