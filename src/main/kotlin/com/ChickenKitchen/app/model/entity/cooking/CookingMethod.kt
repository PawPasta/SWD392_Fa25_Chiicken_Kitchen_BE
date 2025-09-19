package com.ChickenKitchen.app.model.entity.cooking

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "cooking_methods")
class CookingMethod(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Lob
    var note: String? = null,

    @Column(nullable = false, precision = 15, scale = 2)
    var basePrice: BigDecimal = BigDecimal.ZERO,

    @OneToMany(mappedBy = "method", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var cooking_effects: MutableList<CookingEffect> = mutableListOf(),
)
