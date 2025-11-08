package com.ChickenKitchen.app.repository.step

import com.ChickenKitchen.app.model.dto.response.PopularDishResponse
import com.ChickenKitchen.app.model.entity.step.Dish
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Repository
interface DishRepository : JpaRepository<Dish, Long>, JpaSpecificationExecutor<Dish> {

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

    // Only sample (non-custom) dishes
    fun findAllByIsCustomFalse(sort: Sort): List<Dish>
    fun findAllByIsCustomFalse(pageable: Pageable): Page<Dish>

    // By components (menu items)
    @Query(
        "select distinct d from Dish d join d.orderSteps os join os.items it join it.menuItem mi " +
        "where d.isCustom = false and mi.id in :menuItemIds order by d.createdAt desc"
    )
    fun findAllByMenuItemIds(@Param("menuItemIds") menuItemIds: List<Long>, pageable: Pageable): Page<Dish>

    @Query(
        "select distinct d from Dish d join d.orderSteps os join os.items it join it.menuItem mi " +
        "where d.isCustom = false and mi.id in :menuItemIds order by d.createdAt desc"
    )
    fun findAllByMenuItemIds(@Param("menuItemIds") menuItemIds: List<Long>): List<Dish>

    @Query(
        "select distinct d from Dish d join d.orderSteps os join os.items it join it.menuItem mi " +
        "where d.isCustom = false and lower(mi.name) like lower(concat('%', :keyword, '%')) order by d.createdAt desc"
    )
    fun findAllByMenuItemName(@Param("keyword") keyword: String, pageable: Pageable): Page<Dish>

    @Query(
        "select distinct d from Dish d join d.orderSteps os join os.items it join it.menuItem mi " +
        "where d.isCustom = false and lower(mi.name) like lower(concat('%', :keyword, '%')) order by d.createdAt desc"
    )
    fun findAllByMenuItemName(@Param("keyword") keyword: String): List<Dish>

    // By calories
    fun findAllByIsCustomFalseAndCalBetween(min: Int, max: Int, pageable: Pageable): Page<Dish>
    fun findAllByIsCustomFalseAndCalBetween(min: Int, max: Int, sort: Sort): List<Dish>
    fun findAllByIsCustomFalseAndCalGreaterThanEqual(min: Int, pageable: Pageable): Page<Dish>
    fun findAllByIsCustomFalseAndCalGreaterThanEqual(min: Int, sort: Sort): List<Dish>
    fun findAllByIsCustomFalseAndCalLessThanEqual(max: Int, pageable: Pageable): Page<Dish>
    fun findAllByIsCustomFalseAndCalLessThanEqual(max: Int, sort: Sort): List<Dish>

    // By price
    fun findAllByIsCustomFalseAndPriceBetween(min: Int, max: Int, pageable: Pageable): Page<Dish>
    fun findAllByIsCustomFalseAndPriceBetween(min: Int, max: Int, sort: Sort): List<Dish>
    fun findAllByIsCustomFalseAndPriceGreaterThanEqual(min: Int, pageable: Pageable): Page<Dish>
    fun findAllByIsCustomFalseAndPriceGreaterThanEqual(min: Int, sort: Sort): List<Dish>
    fun findAllByIsCustomFalseAndPriceLessThanEqual(max: Int, pageable: Pageable): Page<Dish>
    fun findAllByIsCustomFalseAndPriceLessThanEqual(max: Int, sort: Sort): List<Dish>

    // Custom dishes ordered by a given user
    @Query(
        "select d from OrderDish od join od.dish d join od.order o " +
        "where o.user.email = :email and d.isCustom = true order by d.createdAt desc"
    )
    fun findAllCustomByUserEmailOrderByCreatedAtDesc(@Param("email") email: String): List<Dish>

    @Query("""
    SELECT new com.ChickenKitchen.app.model.dto.response.PopularDishResponse(
        d.id, d.name, COUNT(od.id), SUM(o.totalPrice), 'N/A'
    )
        FROM OrderDish od
        JOIN od.dish d
        JOIN od.order o
        WHERE o.createdAt BETWEEN :startDate AND :endDate
        GROUP BY d.id, d.name
        ORDER BY SUM(o.totalPrice) DESC
""")
    fun findPopularDishes(
        @Param("startDate") startDate: Timestamp,
        @Param("endDate") endDate: Timestamp
    ): List<PopularDishResponse>
}
