package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.promotion

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.promotion.PromotionOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PromotionOrderRepository : JpaRepository<PromotionOrder, Long> {
    fun findAllByUserId(userId: Long): List<PromotionOrder>
    fun findAllByOrderId(orderId: Long): List<PromotionOrder>
    fun findAllByPromotionId(promotionId: Long): List<PromotionOrder>
}
