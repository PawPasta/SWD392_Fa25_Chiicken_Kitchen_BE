package com.Chicken_Kitchen.repository.promotion

import com.Chicken_Kitchen.model.entity.promotion.Promotion
import com.Chicken_Kitchen.enum.DiscountType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PromotionRepository : JpaRepository<Promotion, Long> {
    fun findAllByIsActive(isActive: Boolean = true): List<Promotion>
    fun findAllByDiscountType(discountType: DiscountType): List<Promotion>
    fun findByIdAndIsActive(id: Long, isActive: Boolean = true): Optional<Promotion>
}
