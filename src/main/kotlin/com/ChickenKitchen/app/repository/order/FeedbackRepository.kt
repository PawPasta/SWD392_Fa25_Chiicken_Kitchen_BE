package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.model.entity.order.Feedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findByOrderId(orderId: Long): Optional<Feedback>
    fun findAllByUserId(userId: Long): List<Feedback>
}
