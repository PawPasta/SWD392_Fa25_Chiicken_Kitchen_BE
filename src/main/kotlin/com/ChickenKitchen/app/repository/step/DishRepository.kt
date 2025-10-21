package com.ChickenKitchen.app.repository.step

import com.ChickenKitchen.app.model.entity.step.Dish
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface DishRepository : JpaRepository<Dish, Long> {
    fun findAllByOrderId(orderId: Long): List<Dish>

    @Transactional
    fun deleteByOrderId(orderId: Long): Long
}
