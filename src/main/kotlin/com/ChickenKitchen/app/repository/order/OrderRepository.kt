package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.dto.response.OrderPickupRevenueProjection
import com.ChickenKitchen.app.model.entity.order.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface OrderRepository : JpaRepository<Order, Long>{
    fun findFirstByUserEmailAndStoreIdAndStatusOrderByCreatedAtDesc(email: String, storeId: Long, status: OrderStatus): Order?

    fun findAllByUserEmailAndStoreIdAndStatusInOrderByCreatedAtDesc(
        email: String,
        storeId: Long,
        statuses: List<OrderStatus>
    ): List<Order>

    fun findAllByStoreIdAndStatus(storeId: Long, status: OrderStatus, pageable: Pageable): Page<Order>

    @Query(
        "select o from Order o join o.user u " +
        "where o.store.id = :storeId and o.status = :status and (" +
        "lower(u.fullName) like lower(concat('%', :keyword, '%')) or " +
        "lower(u.email) like lower(concat('%', :keyword, '%'))" +
        ")"
    )
    fun searchAllByStoreIdAndStatus(
        @Param("storeId") storeId: Long,
        @Param("status") status: OrderStatus,
        @Param("keyword") keyword: String,
        pageable: Pageable
    ): Page<Order>
    fun findAllByStoreIdAndStatusOrderByCreatedAtDesc(
        storeId: Long,
        status: OrderStatus
    ): List<Order>

    @Query(
        """
        SELECT new com.ChickenKitchen.app.model.dto.response.OrderPickupRevenueProjection(
            o.pickupTime, SUM(o.totalPrice)
        )
        FROM Order o
        WHERE o.pickupTime >= :fromDate
        GROUP BY o.pickupTime
        ORDER BY o.pickupTime DESC
        """
    )
    fun findPickupAndTotalSince(
        @Param("fromDate") fromDate: Timestamp
    ): List<OrderPickupRevenueProjection>

    @Query("""
        SELECT SUM(o.totalPrice)
        FROM Order o
        WHERE o.createdAt BETWEEN :startDate AND :endDate
    """)
    fun getTotalRevenue(
        @Param("startDate") startDate: Timestamp,
        @Param("endDate") endDate: Timestamp
    ): Long?

    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.createdAt BETWEEN :startDate AND :endDate
    """)
    fun getTotalOrders(
        @Param("startDate") startDate: Timestamp,
        @Param("endDate") endDate: Timestamp
    ): Long?

    @Query("""
        SELECT COUNT(DISTINCT o.user.id)
        FROM Order o
        WHERE o.createdAt BETWEEN :startDate AND :endDate
    """)
    fun getTotalCustomers(
        @Param("startDate") startDate: Timestamp,
        @Param("endDate") endDate: Timestamp
    ): Long?
}
