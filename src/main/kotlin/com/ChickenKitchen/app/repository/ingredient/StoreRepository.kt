package com.ChickenKitchen.app.repository.ingredient

import com.ChickenKitchen.app.model.entity.ingredient.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface StoreRepository : JpaRepository<Store, Long> , JpaSpecificationExecutor<Store> {

    fun findByName(name: String) : Store?
    fun findByAddress(address: String) : Store?

    @Query("""
    SELECT s 
    FROM Store s 
    JOIN s.orders o 
    GROUP BY s 
    ORDER BY SUM(o.totalPrice) DESC
""")
    fun findStoreWithHighestTotalPrice(): List<Store>
}