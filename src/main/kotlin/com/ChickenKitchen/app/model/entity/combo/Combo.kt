package com.ChickenKitchen.app.model.entity.combo

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "combos")
class Combo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, precision = 15, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var cal: Int,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "combo", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var combo_items: MutableList<ComboItem> = mutableListOf(),
)
