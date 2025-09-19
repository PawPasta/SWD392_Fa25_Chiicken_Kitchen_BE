package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.combo

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.recipe.Recipe
import jakarta.persistence.*

@Entity
@Table(name = "combo_items")
class ComboItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_id", nullable = false)
    var combo: Combo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    var recipe: Recipe,

    @Column(nullable = false)
    var quantity: Int
)
