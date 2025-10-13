package com.ChickenKitchen.app.repository.promotion

import com.ChickenKitchen.app.model.entity.promotion.Promotion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PromotionRepository : JpaRepository<Promotion, Long> {

}