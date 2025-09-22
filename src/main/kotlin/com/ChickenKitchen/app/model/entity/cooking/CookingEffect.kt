package com.ChickenKitchen.app.model.entity.cooking

import jakarta.persistence.*
import com.ChickenKitchen.app.enum.EffectType
import com.ChickenKitchen.app.model.entity.cooking.CookingMethod
import com.ChickenKitchen.app.model.entity.ingredient.Nutrient
import java.math.BigDecimal

@Entity
@Table(name = "cooking_effects")
class CookingEffect(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_id", nullable = false)
    var method: CookingMethod,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrient_id", nullable = false)
    var nutrient: Nutrient,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var effectType: EffectType, // Enum: INCREASE, DECREASE

    @Column(nullable = false)
    var value: BigDecimal, // % thay đổi giá trị

    var description: String? = null
)
