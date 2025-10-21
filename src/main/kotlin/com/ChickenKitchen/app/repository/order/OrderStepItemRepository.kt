package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.model.entity.order.OrderStepItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface OrderStepItemRepository : JpaRepository<OrderStepItem, Long> {
    fun countByDailyMenuItemMenuItemId(menuItemId: Long): Long

    @Transactional
    @Modifying
    @Query("delete from OrderStepItem osi where osi.orderStep.dish.order.id = :orderId")
    fun deleteByOrderId(@Param("orderId") orderId: Long): Int

    @Transactional
    @Modifying
    @Query("delete from OrderStepItem osi where osi.orderStep.dish.id = :dishId")
    fun deleteByDishId(@Param("dishId") dishId: Long): Int
}
