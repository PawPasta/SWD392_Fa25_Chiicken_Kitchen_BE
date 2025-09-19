package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.promotion

import jakarta.persistence.*
import java.math.BigDecimal
import java.sql.Timestamp
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.enum.DiscountType

@Entity
@Table(name = "promotions")
class Promotion(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var discountType: DiscountType,   // Enum: PERCENT, AMOUNT...

    @Column(nullable = false, precision = 10, scale = 2)
    var discountValue: BigDecimal,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @Column(nullable = false)
    var startDate: Timestamp,

    @Column(nullable = false)
    var endDate: Timestamp,

    @Column(nullable = false)
    var quantity: Int,

    @OneToMany(mappedBy = "promotion", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var promotion_orders: MutableList<PromotionOrder> = mutableListOf(),
)
