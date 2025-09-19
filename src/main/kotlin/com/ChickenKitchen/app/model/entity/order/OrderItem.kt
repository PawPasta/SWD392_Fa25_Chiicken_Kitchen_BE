package com.ChickenKitchen.app.model.entity.order

import jakarta.persistence.*
import java.time.LocalDate
import com.ChickenKitchen.app.enum.ItemType
import com.ChickenKitchen.app.model.entity.recipe.Recipe
import java.math.BigDecimal
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "order_items")
class OrderItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var itemType: ItemType, // ENUM: meal, custom_meal, drink...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "recipe_id", nullable = false)
    var recipe: Recipe,

    @Column(nullable = false)
    var quantity: Int,

    @Column(columnDefinition = "JSON")
    var recipeSnapshot: String? = null,

    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: LocalDate,

    @Column(nullable = false)
    var lineTotalPrice: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var lineTotalCal: Int,

    @Lob
    var note: String? = null
)
