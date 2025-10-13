package com.ChickenKitchen.app.repository.promotion

import com.ChickenKitchen.app.model.entity.promotion.OrderPromotion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface OrderPromotionRepository : JpaRepository<OrderPromotion, Long> {
}