package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.response.AddDishResponse
import com.ChickenKitchen.app.model.dto.response.CreatedOrderStep
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.order.OrderStep
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
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
    private val dishRepository: com.ChickenKitchen.app.repository.step.DishRepository,
) : OrderService {

    @Transactional
    override fun addDishToCurrentOrder(req: CreateDishRequest): AddDishResponse {
        require(req.selections.isNotEmpty()) { "Dish must contain at least one step selection" }

        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("User not found")
        val store = storeRepository.findById(req.storeId).orElseThrow { NoSuchElementException("Store with id ${req.storeId} not found") }

        // Find current NEW order or create one
        var order = orderRepository.findFirstByUserEmailAndStoreIdAndStatusOrderByCreatedAtDesc(
            email, store.id!!, OrderStatus.NEW
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
            order = order,
            name = req.name,
            isCustomizable = req.isCustomizable,
            isActive = true
        )
        // Persist dish using DishRepository
        val savedDish = dishRepository.save(dish)

        // Build steps sorted by stepNumber
        val stepMap = stepRepository.findAllById(req.selections.map { it.stepId }).associateBy { it.id!! }
        val sortedSelections = req.selections.sortedBy { stepMap[it.stepId]?.stepNumber ?: Int.MAX_VALUE }

        val created = mutableListOf<CreatedOrderStep>()
        val orderStepsToSave = mutableListOf<OrderStep>()
        for (sel in sortedSelections) {
            val step = stepMap[sel.stepId] ?: throw NoSuchElementException("Step with id ${sel.stepId} not found")
            require(sel.items.isNotEmpty()) { "Each step selection must include at least one menu item" }
            sel.items.forEach { item ->
                require(item.quantity > 0) { "Quantity must be > 0" }
                val menuItem = menuItemRepository.findById(item.menuItemId)
                    .orElseThrow { NoSuchElementException("Menu item with id ${item.menuItemId} not found") }
                orderStepsToSave.add(
                    OrderStep(
                        order = order,
                        step = step,
                        menuItem = menuItem,
                        quantity = item.quantity
                    )
                )
            }
        }

        val savedLines = orderStepRepository.saveAll(orderStepsToSave)
        savedLines.forEach { line ->
            created.add(
                CreatedOrderStep(
                    id = line.id!!,
                    stepId = line.step.id!!,
                    menuItemId = line.menuItem.id!!,
                    quantity = line.quantity
                )
            )
        }

        return AddDishResponse(
            orderId = order.id!!,
            dishId = savedDish.id!!,
            status = order.status.name,
            createdSteps = created
        )
    }
    
}
