package com.ChickenKitchen.app.model.entity.cooking

import jakarta.persistence.*
import com.ChickenKitchen.app.enum.EffectType
import com.ChickenKitchen.app.model.entity.cooking.CookingMethod

@Entity
@Table(name = "cooking_effects")
class CookingEffect(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_id", nullable = false)
    var method: CookingMethod,

    @Column(name = "nutrient_id", nullable = false)
    var nutrientId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var effectType: EffectType, // Enum: INCREASE, DECREASE

    @Column(nullable = false)
    var value: Int, // % thay đổi giá trị

    var description: String? = null
)
