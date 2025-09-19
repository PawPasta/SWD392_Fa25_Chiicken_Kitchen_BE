package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.order

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.order.OrderItem
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.enum.ItemType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderItemRepository : JpaRepository<OrderItem, Long> {
    fun findAllByOrderId(orderId: Long): List<OrderItem>
    fun findAllByRecipeId(recipeId: Long): List<OrderItem>
    fun findAllByItemType(itemType: ItemType): List<OrderItem>
}
