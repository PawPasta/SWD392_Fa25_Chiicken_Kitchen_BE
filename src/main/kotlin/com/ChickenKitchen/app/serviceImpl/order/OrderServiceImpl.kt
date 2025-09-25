package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enum.OrderStatus
import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.handler.QuantityMustBeNonNegativeException
import com.ChickenKitchen.app.mapper.toOrderDetailResponse
import com.ChickenKitchen.app.mapper.toOrderResponse
import com.ChickenKitchen.app.mapper.toOrderResponseList
import com.ChickenKitchen.app.model.dto.request.CreateOrderRequest
import com.ChickenKitchen.app.model.dto.request.OrderItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateOrderRequest
import com.ChickenKitchen.app.model.dto.response.OrderDetailResponse
import com.ChickenKitchen.app.model.dto.response.OrderResponse
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.order.OrderItem
import com.ChickenKitchen.app.model.entity.order.OrderStatusHistory
import com.ChickenKitchen.app.repository.menu.DailyMenuItemRepository
import com.ChickenKitchen.app.repository.order.OrderItemRepository
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.order.OrderStatusHistoryRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.order.OrderService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDate

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val statusHistoryRepository: OrderStatusHistoryRepository,
    private val userRepository: UserRepository,
    private val dailyMenuItemRepository: DailyMenuItemRepository
) : OrderService {

    override fun getAll(): List<OrderResponse>? {
        val list = orderRepository.findAll()
        if (list.isEmpty()) return null
        return list.toOrderResponseList()
    }

    override fun getById(id: Long): OrderDetailResponse {
        val order = orderRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Order with id $id not found")
        return order.toOrderDetailResponse()
    }

    override fun create(req: CreateOrderRequest): OrderDetailResponse {
        val user = userRepository.findById(req.userId).orElse(null)
            ?: throw IngredientNotFoundException("User with id ${req.userId} not found")

        val order = Order(
            user = user,
            totalPrice = BigDecimal.ZERO,
            status = req.status,
            createdAt = Timestamp(System.currentTimeMillis())
        )

        applyItems(order, req.items)
        addStatusHistory(order, req.status, note = "Order created")

        val saved = orderRepository.save(order)
        return saved.toOrderDetailResponse()
    }

    override fun update(id: Long, req: UpdateOrderRequest): OrderDetailResponse {
        val order = orderRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Order with id $id not found")

        req.items?.let { applyItems(order, it) }
        req.status?.let { 
            order.status = it
            addStatusHistory(order, it, note = "Status updated")
        }

        val saved = orderRepository.save(order)
        return saved.toOrderDetailResponse()
    }

    override fun delete(id: Long) {
        val order = orderRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Order with id $id not found")
        orderRepository.delete(order)
    }

    override fun changeStatus(id: Long): OrderResponse {
        val order = orderRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Order with id $id not found")
        val next = nextStatus(order.status)
        order.status = next
        addStatusHistory(order, next, note = "Status changed by toggle")
        val updated = orderRepository.save(order)
        return updated.toOrderResponse()
    }

    private fun applyItems(order: Order, items: List<OrderItemRequest>) {
        val newItems = items.map { reqItem ->
            val dmi = dailyMenuItemRepository.findById(reqItem.dailyMenuItemId).orElse(null)
                ?: throw IngredientNotFoundException("DailyMenuItem with id ${reqItem.dailyMenuItemId} not found")
            if (reqItem.quantity < 0) throw QuantityMustBeNonNegativeException("Quantity must be non-negative")
            val linePrice = dmi.price.multiply(BigDecimal(reqItem.quantity))
            val lineCal = dmi.cal * reqItem.quantity
            OrderItem(
                order = order,
                dailyMenuItem = dmi,
                quantity = reqItem.quantity,
                price = linePrice,
                cal = lineCal,
                note = reqItem.note
            )
        }

        order.order_items.apply {
            clear()
            addAll(newItems)
        }

        val total = order.order_items.fold(BigDecimal.ZERO) { acc, it -> acc + it.price }
        order.totalPrice = total
    }

    private fun addStatusHistory(order: Order, status: OrderStatus, note: String? = null) {
        val history = OrderStatusHistory(
            order = order,
            status = status,
            changeAt = LocalDate.now(),
            changeBy = LocalDate.now(),
            note = note
        )
        order.order_status_history.add(history)
    }

    private fun nextStatus(current: OrderStatus): OrderStatus = when (current) {
        OrderStatus.PENDING -> OrderStatus.CONFIRMED
        OrderStatus.CONFIRMED -> OrderStatus.PREPARING
        OrderStatus.PREPARING -> OrderStatus.READY
        OrderStatus.READY -> OrderStatus.DELIVERING
        OrderStatus.DELIVERING -> OrderStatus.COMPLETED
        OrderStatus.COMPLETED -> OrderStatus.COMPLETED
        OrderStatus.CANCELED -> OrderStatus.CANCELED
    }
}

