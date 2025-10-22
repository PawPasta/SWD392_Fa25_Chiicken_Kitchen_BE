package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.response.AddDishResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse
import com.ChickenKitchen.app.model.dto.response.CurrentDishResponse
import com.ChickenKitchen.app.model.dto.response.CurrentStepResponse
import com.ChickenKitchen.app.model.dto.response.CurrentStepItemResponse
import com.ChickenKitchen.app.model.dto.response.OrderBriefResponse
import com.ChickenKitchen.app.model.dto.response.CreatedOrderStep
import com.ChickenKitchen.app.model.dto.response.CreatedStepItem
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.order.OrderStep
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuItemRepository
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuRepository
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.order.OrderStepRepository
import com.ChickenKitchen.app.repository.order.OrderStepItemRepository
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
    private val orderStepItemRepository: OrderStepItemRepository,
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val stepRepository: StepRepository,
    private val menuItemRepository: MenuItemRepository,
    private val dailyMenuItemRepository: DailyMenuItemRepository,
    private val dailyMenuRepository: DailyMenuRepository,
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

        // Create dish entry with temporary totals = 0
        val savedDish = dishRepository.save(
            Dish(
                order = order!!,
                price = 0,
                cal = 0,
                note = req.note
            )
        )

        // Build steps sorted by stepNumber
        val stepMap = stepRepository.findAllById(req.selections.map { it.stepId }).associateBy { it.id!! }
        val sortedSelections = req.selections.sortedBy { stepMap[it.stepId]?.stepNumber ?: Int.MAX_VALUE }

        val created = mutableListOf<CreatedOrderStep>()
        val savedSteps = mutableListOf<OrderStep>()
        var totalPrice = 0
        var totalCal = 0
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
                // FE sends MenuItem ID
                val menuItem = menuItemRepository.findById(item.menuItemId)
                    .orElseThrow { NoSuchElementException("Menu item with id ${item.menuItemId} not found") }
                if (menuItem.category.id != step.category.id) {
                    throw IllegalArgumentException("MenuItem ${menuItem.id} does not belong to step ${step.id} category")
                }
                val today = java.time.LocalDate.now()
                val start = java.sql.Timestamp.valueOf(today.atStartOfDay())
                val end = java.sql.Timestamp.valueOf(today.atTime(23, 59, 59))
                val todaysMenu = dailyMenuRepository.findByStoreAndDateRange(store.id!!, start, end)
                    ?: throw NoSuchElementException("No daily menu for store ${store.id} today; cannot resolve menu item ${item.menuItemId}")
                val dmi = todaysMenu.dailyMenuItems.firstOrNull { it.menuItem.id == item.menuItemId }
                    ?: throw NoSuchElementException("Menu item ${item.menuItemId} not in today's daily menu for store ${store.id}")

                val link = com.ChickenKitchen.app.model.entity.order.OrderStepItem(
                    orderStep = orderStep,
                    dailyMenuItem = dmi,
                    quantity = item.quantity
                )
                orderStep.items.add(link)
                createdItems.add(CreatedStepItem(dailyMenuItemId = dmi.id!!, quantity = item.quantity))

                // Accumulate totals from MenuItem base values
                totalPrice += (menuItem.price * item.quantity)
                totalCal += (menuItem.cal * item.quantity)
            }
            // Save with items
            orderStepRepository.save(orderStep)
            created.add(CreatedOrderStep(id = orderStep.id!!, stepId = step.id!!, items = createdItems))
        }

        // Update dish totals
        savedDish.price = totalPrice
        savedDish.cal = totalCal
        dishRepository.save(savedDish)

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
                cleared = false,
                dishes = emptyList()
            )
        }

        // If this NEW order was created not today, clear all items
        val createdAt = order.createdAt?.toLocalDateTime()?.toLocalDate()
        val isToday = createdAt == java.time.LocalDate.now()
        if (!isToday) {
            orderStepItemRepository.deleteByOrderId(order.id!!)
            orderStepRepository.deleteByDishOrderId(order.id!!)
            dishRepository.deleteByOrderId(order.id!!)
            return OrderCurrentResponse(order.id!!, order.status.name, true, emptyList())
        }

        // Only include dishes updated today
        val today = java.time.LocalDate.now()
        val start = java.sql.Timestamp.valueOf(today.atStartOfDay())
        val end = java.sql.Timestamp.valueOf(today.atTime(23, 59, 59))
        val dishesToday = dishRepository.findAllByOrderIdAndUpdatedAtBetween(order.id!!, start, end)

        val dishResponses = dishesToday.map { d ->
            val steps = orderStepRepository.findAllByDishId(d.id!!)
            val stepResponses = steps.map { st ->
                val itemResponses = st.items.map { link ->
                    val mi = link.dailyMenuItem.menuItem
                    CurrentStepItemResponse(
                        dailyMenuItemId = link.dailyMenuItem.id!!,
                        menuItemId = mi.id!!,
                        menuItemName = mi.name,
                        quantity = link.quantity,
                        price = mi.price,
                        cal = mi.cal
                    )
                }
                CurrentStepResponse(
                    stepId = st.step.id!!,
                    stepName = st.step.name,
                    items = itemResponses
                )
            }
            CurrentDishResponse(
                dishId = d.id!!,
                note = d.note,
                price = d.price,
                cal = d.cal,
                updatedAt = d.updatedAt,
                steps = stepResponses
            )
        }

        return OrderCurrentResponse(order.id!!, order.status.name, false, dishResponses)
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

    @Transactional
    override fun updateDish(dishId: Long, req: com.ChickenKitchen.app.model.dto.request.UpdateDishRequest): AddDishResponse {
        require(req.selections.isNotEmpty()) { "Dish must contain at least one step selection" }

        val dish = dishRepository.findById(dishId).orElseThrow { NoSuchElementException("Dish with id $dishId not found") }
        val order = dish.order
        val store = order.store

        // Clear existing items for this dish
        orderStepItemRepository.deleteByDishId(dishId)
        orderStepRepository.deleteByDishId(dishId)

        // Optionally update note
        dish.note = req.note ?: dish.note

        val stepMap = stepRepository.findAllById(req.selections.map { it.stepId }).associateBy { it.id!! }
        val sortedSelections = req.selections.sortedBy { stepMap[it.stepId]?.stepNumber ?: Int.MAX_VALUE }

        var totalPrice = 0
        var totalCal = 0
        val created = mutableListOf<CreatedOrderStep>()
        for (sel in sortedSelections) {
            val step = stepMap[sel.stepId] ?: throw NoSuchElementException("Step with id ${sel.stepId} not found")
            require(sel.items.isNotEmpty()) { "Each step selection must include at least one item" }

            val orderStep = orderStepRepository.save(
                OrderStep(
                    dish = dish,
                    step = step
                )
            )

            val createdItems = mutableListOf<CreatedStepItem>()
            sel.items.forEach { item ->
                require(item.quantity > 0) { "Quantity must be > 0" }
                val menuItem = menuItemRepository.findById(item.menuItemId)
                    .orElseThrow { NoSuchElementException("Menu item with id ${item.menuItemId} not found") }
                if (menuItem.category.id != step.category.id) {
                    throw IllegalArgumentException("MenuItem ${menuItem.id} does not belong to step ${step.id} category")
                }

                val today = java.time.LocalDate.now()
                val start = java.sql.Timestamp.valueOf(today.atStartOfDay())
                val end = java.sql.Timestamp.valueOf(today.atTime(23, 59, 59))
                val todaysMenu = dailyMenuRepository.findByStoreAndDateRange(store.id!!, start, end)
                    ?: throw NoSuchElementException("No daily menu for store ${store.id} today; cannot resolve menu item ${item.menuItemId}")
                val dmi = todaysMenu.dailyMenuItems.firstOrNull { it.menuItem.id == item.menuItemId }
                    ?: throw NoSuchElementException("Menu item ${item.menuItemId} not in today's daily menu for store ${store.id}")

                val link = com.ChickenKitchen.app.model.entity.order.OrderStepItem(
                    orderStep = orderStep,
                    dailyMenuItem = dmi,
                    quantity = item.quantity
                )
                orderStep.items.add(link)
                createdItems.add(CreatedStepItem(dailyMenuItemId = dmi.id!!, quantity = item.quantity))

                totalPrice += (menuItem.price * item.quantity)
                totalCal += (menuItem.cal * item.quantity)
            }
            orderStepRepository.save(orderStep)
            created.add(CreatedOrderStep(id = orderStep.id!!, stepId = step.id!!, items = createdItems))
        }

        // Update dish note/price/cal
        dish.price = totalPrice
        dish.cal = totalCal
        // can't reassign val note; in entity Dish 'note' is val; need to update entity to var
        // Workaround: persist via copy not available; adjust entity to mutable note
        dishRepository.save(dish)

        return AddDishResponse(
            orderId = order.id!!,
            dishId = dish.id!!,
            status = order.status.name,
            createdSteps = created
        )
    }

    @Transactional
    override fun deleteDish(dishId: Long): Long {
        val dish = dishRepository.findById(dishId).orElseThrow { NoSuchElementException("Dish with id $dishId not found") }
        val orderId = dish.order.id!!
        orderStepItemRepository.deleteByDishId(dishId)
        orderStepRepository.deleteByDishId(dishId)
        dishRepository.delete(dish)
        return orderId
    }
}
