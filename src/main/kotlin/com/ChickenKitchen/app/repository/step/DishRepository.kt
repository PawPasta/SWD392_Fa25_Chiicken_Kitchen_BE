package com.ChickenKitchen.app.repository.step

import com.ChickenKitchen.app.model.entity.step.Dish
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface DishRepository : JpaRepository<Dish, Long> {

    @Query("select d from OrderDish od join od.dish d where od.order.id = :orderId")
    fun findAllByOrderId(@Param("orderId") orderId: Long): List<Dish>

    @Query(
        "select d from OrderDish od join od.dish d " +
        "where od.order.id = :orderId and d.updatedAt between :start and :end"
    )
    fun findAllByOrderIdAndUpdatedAtBetween(
        @Param("orderId") orderId: Long,
        @Param("start") start: java.sql.Timestamp,
        @Param("end") end: java.sql.Timestamp
    ): List<Dish>

    // Delete only custom dishes linked to the order; links will be removed separately
    @Transactional
    @Modifying
    @Query(
        "delete from Dish d where d.isCustom = true and d.id in (" +
        "  select od.dish.id from OrderDish od where od.order.id = :orderId" +
        ")"
    )
    fun deleteByOrderId(@Param("orderId") orderId: Long): Int
}
