package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.model.entity.order.OrderStep
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface OrderStepRepository : JpaRepository<OrderStep, Long> {

    fun findAllByDishOrderId(orderId: Long): List<OrderStep>
    fun findAllByDishId(dishId: Long): List<OrderStep>

    @Transactional
    @Modifying
    @Query("delete from OrderStep os where os.dish.order.id = :orderId")
    fun deleteByDishOrderId(@Param("orderId") orderId: Long): Int

    @Transactional
    @Modifying
    @Query("delete from OrderStep os where os.dish.id = :dishId")
    fun deleteByDishId(@Param("dishId") dishId: Long): Int
}
