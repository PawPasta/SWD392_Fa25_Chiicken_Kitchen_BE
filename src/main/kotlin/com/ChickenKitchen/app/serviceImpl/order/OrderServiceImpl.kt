package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.response.AddDishResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse
import com.ChickenKitchen.app.model.dto.response.OrderBriefResponse
import com.ChickenKitchen.app.model.dto.response.CreatedOrderStep
import com.ChickenKitchen.app.model.dto.response.CreatedStepItem
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.order.OrderStep
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuItemRepository
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.order.OrderStepRepository
import com.ChickenKitchen.app.repository.step.StepRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.order.OrderService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderStepRepository: OrderStepRepository,
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val stepRepository: StepRepository,
    private val menuItemRepository: MenuItemRepository,
    private val dailyMenuItemRepository: DailyMenuItemRepository,
    private val dishRepository: com.ChickenKitchen.app.repository.step.DishRepository,
) : OrderService {

    @Transactional
    override fun addDishToCurrentOrder(req: CreateDishRequest): AddDishResponse {
        require(req.selections.isNotEmpty()) { "Dish must contain at least one step selection" }

        val principal = SecurityContextHolder.getContext().authentication
        val email = principal?.name
        val effectiveEmail = if (email != null && email.contains("@")) email else "chickenkitchen785@gmail.com"
        val user = userRepository.findByEmail(effectiveEmail)
            ?: userRepository.findAll().firstOrNull()
            ?: throw RuntimeException("User not found")
        val store = storeRepository.findById(req.storeId).orElseThrow { NoSuchElementException("Store with id ${req.storeId} not found") }

        // Find current NEW order or create one
        var order = orderRepository.findFirstByUserEmailAndStoreIdAndStatusOrderByCreatedAtDesc(
            user.email, store.id!!, OrderStatus.NEW
        )
        if (order == null) {
            order = orderRepository.save(
                Order(
                    user = user,
                    store = store,
                    totalPrice = 0,
                    status = OrderStatus.NEW,
                    pickupTime = Timestamp(System.currentTimeMillis()),
                )
            )
        }

        // Create dish entry
        val dish = Dish(
            order = order!!,
            price = req.price,
            cal = req.cal,
            note = req.note
        )
        // Persist dish using DishRepository
        val savedDish = dishRepository.save(dish)

        // Build steps sorted by stepNumber
        val stepMap = stepRepository.findAllById(req.selections.map { it.stepId }).associateBy { it.id!! }
        val sortedSelections = req.selections.sortedBy { stepMap[it.stepId]?.stepNumber ?: Int.MAX_VALUE }

        val created = mutableListOf<CreatedOrderStep>()
        val savedSteps = mutableListOf<OrderStep>()
        for (sel in sortedSelections) {
            val step = stepMap[sel.stepId] ?: throw NoSuchElementException("Step with id ${sel.stepId} not found")
            require(sel.items.isNotEmpty()) { "Each step selection must include at least one item" }

            val orderStep = orderStepRepository.save(
                OrderStep(
                    dish = savedDish,
                    step = step,
                )
            )
            savedSteps.add(orderStep)

            val createdItems = mutableListOf<CreatedStepItem>()
            sel.items.forEach { item ->
                require(item.quantity > 0) { "Quantity must be > 0" }
                val dmi = dailyMenuItemRepository.findById(item.dailyMenuItemId)
                    .orElseThrow { NoSuchElementException("Daily menu item with id ${item.dailyMenuItemId} not found") }
                val link = com.ChickenKitchen.app.model.entity.order.OrderStepItem(
                    orderStep = orderStep,
                    dailyMenuItem = dmi,
                    quantity = item.quantity
                )
                // Persist link
                // Because OrderStep.items is cascade ALL, add to collection is enough if orderStep managed
                orderStep.items.add(link)
                createdItems.add(CreatedStepItem(dailyMenuItemId = dmi.id!!, quantity = item.quantity))
            }
            // Save with items
            orderStepRepository.save(orderStep)
            created.add(CreatedOrderStep(id = orderStep.id!!, stepId = step.id!!, items = createdItems))
        }

        return AddDishResponse(
            orderId = order.id!!,
            dishId = savedDish.id!!,
            status = order.status.name,
            createdSteps = created
        )
    }
    
    @Transactional
    override fun getCurrentOrderForStore(storeId: Long): OrderCurrentResponse {
        val principal = SecurityContextHolder.getContext().authentication
        val email = principal?.name
        val effectiveEmail = if (email != null && email.contains("@")) email else "chickenkitchen785@gmail.com"
        val user = userRepository.findByEmail(effectiveEmail)
            ?: userRepository.findAll().firstOrNull()
            ?: throw RuntimeException("User not found")
        val store = storeRepository.findById(storeId).orElseThrow { NoSuchElementException("Store with id $storeId not found") }

        var order = orderRepository.findFirstByUserEmailAndStoreIdAndStatusOrderByCreatedAtDesc(
            user.email, store.id!!, OrderStatus.NEW
        )
        if (order == null) {
            order = orderRepository.save(
                Order(
                    user = user,
                    store = store,
                    totalPrice = 0,
                    status = OrderStatus.NEW,
                    pickupTime = java.sql.Timestamp(System.currentTimeMillis()),
                )
            )
            return OrderCurrentResponse(
                orderId = order.id!!,
                status = order.status.name,
                totalItems = 0,
                keptItems = 0,
                cleared = false
            )
        }

        // If this NEW order was created not today, clear all items
        val createdAt = order.createdAt?.toLocalDateTime()?.toLocalDate()
        val isToday = createdAt == java.time.LocalDate.now()
        if (!isToday) {
            orderStepRepository.deleteByDishOrderId(order.id!!)
            dishRepository.deleteByOrderId(order.id!!)
            return OrderCurrentResponse(order.id!!, order.status.name, 0, 0, true)
        }

        // Otherwise, just return counts without filtering
        val lines = orderStepRepository.findAllByDishOrderId(order.id!!)
        val total = lines.size
        return OrderCurrentResponse(order.id!!, order.status.name, total, total, false)
    }

    override fun getOrdersHistory(storeId: Long): List<OrderBriefResponse> {
        val principal = SecurityContextHolder.getContext().authentication
        val email = principal?.name
        val effectiveEmail = if (email != null && email.contains("@")) email else "chickenkitchen785@gmail.com"
        val user = userRepository.findByEmail(effectiveEmail)
            ?: userRepository.findAll().firstOrNull()
            ?: throw RuntimeException("User not found")
        val store = storeRepository.findById(storeId).orElseThrow { NoSuchElementException("Store with id $storeId not found") }

        val statuses = listOf(OrderStatus.COMPLETED, OrderStatus.CANCELLED, OrderStatus.PROCESSING)
        val list = orderRepository.findAllByUserEmailAndStoreIdAndStatusInOrderByCreatedAtDesc(user.email, store.id!!, statuses)
        return list.map { o ->
            OrderBriefResponse(
                orderId = o.id!!,
                storeId = o.store.id!!,
                status = o.status.name,
                totalPrice = o.totalPrice,
                createdAt = o.createdAt,
                pickupTime = o.pickupTime
            )
        }
    }
}
