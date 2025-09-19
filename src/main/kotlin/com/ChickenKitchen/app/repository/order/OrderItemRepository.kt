package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.model.entity.order.OrderItem
import com.ChickenKitchen.app.enum.ItemType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderItemRepository : JpaRepository<OrderItem, Long> {
    fun findAllByOrderId(orderId: Long): List<OrderItem>
    fun findAllByRecipeId(recipeId: Long): List<OrderItem>
    fun findAllByItemType(itemType: ItemType): List<OrderItem>
}
