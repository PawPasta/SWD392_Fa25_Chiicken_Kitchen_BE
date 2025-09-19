package com.ChickenKitchen.app.model.entity.order

import jakarta.persistence.*
import java.time.LocalDate
import java.math.BigDecimal
import org.hibernate.annotations.CreationTimestamp
import com.ChickenKitchen.app.model.entity.combo.Combo

@Entity
@Table(name = "order_combos")
class OrderCombo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_id", nullable = false)
    var combo: Combo,

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

    var discountPercentValue: Int? = null,

    @Lob
    var note: String? = null
)
