package com.ChickenKitchen.app.model.entity.promotion

import com.ChickenKitchen.app.enums.DiscountType
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "promotions",
    indexes = [Index(name = "idx_is_active", columnList = "is_active")]
)
class Promotion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    val discountType: DiscountType,

    @Column(name = "discount_value", nullable = false)
    var discountValue: Int,

    @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_date", nullable = false)
    val startDate: LocalDateTime,

    @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_date", nullable = false)
    var endDate: LocalDateTime,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = false,

    @Column(nullable = false)
    var quantity: Int = 0,

    @OneToMany(mappedBy = "promotion")
    val orderPromotions: MutableList<OrderPromotion> = mutableListOf()
)