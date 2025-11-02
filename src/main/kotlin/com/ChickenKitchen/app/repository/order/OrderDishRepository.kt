package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.model.entity.order.OrderDish
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface OrderDishRepository : JpaRepository<OrderDish, Long> {

    @Transactional
    @Modifying
    @Query("delete from OrderDish od where od.order.id = :orderId")
    fun deleteByOrderId(@Param("orderId") orderId: Long): Int

    @Transactional
    @Modifying
    @Query("delete from OrderDish od where od.dish.id = :dishId")
    fun deleteByDishId(@Param("dishId") dishId: Long): Int

    @Query("select od.order.id from OrderDish od where od.dish.id = :dishId")
    fun findOrderIdByDishId(@Param("dishId") dishId: Long): Long?

    @Query("select od from OrderDish od where od.order.id = :orderId and od.dish.id = :dishId")
    fun findByOrderIdAndDishId(
        @Param("orderId") orderId: Long,
        @Param("dishId") dishId: Long,
    ): OrderDish?

    @Query("select od from OrderDish od where od.order.id = :orderId")
    fun findAllByOrderId(@Param("orderId") orderId: Long): List<OrderDish>
}
