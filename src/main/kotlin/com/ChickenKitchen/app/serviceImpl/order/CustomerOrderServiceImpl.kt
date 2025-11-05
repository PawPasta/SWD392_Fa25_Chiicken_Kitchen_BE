package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.enums.PaymentStatus
import com.ChickenKitchen.app.handler.DeleteDishFailedException
import com.ChickenKitchen.app.handler.DishNotFoundException
import com.ChickenKitchen.app.handler.InvalidOrderStatusException
import com.ChickenKitchen.app.handler.InvalidOrderStepException
import com.ChickenKitchen.app.handler.MenuItemNotFoundException
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.handler.StepNotFoundException
import com.ChickenKitchen.app.handler.StoreNotFoundException
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.model.dto.request.CreateCustomDishRequest
import com.ChickenKitchen.app.model.dto.request.CreateExistingDishRequest
import com.ChickenKitchen.app.model.dto.request.CreateFeedbackRequest
import com.ChickenKitchen.app.model.dto.request.SingleNotificationRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishRequest
import com.ChickenKitchen.app.model.dto.response.AddDishResponse
import com.ChickenKitchen.app.model.dto.response.CreatedOrderStep
import com.ChickenKitchen.app.model.dto.response.CreatedStepItem
import com.ChickenKitchen.app.model.dto.response.CurrentDishResponse
import com.ChickenKitchen.app.model.dto.response.CurrentStepItemResponse
import com.ChickenKitchen.app.model.dto.response.CurrentStepResponse
import com.ChickenKitchen.app.model.dto.response.FeedbackResponse
import com.ChickenKitchen.app.model.dto.response.OrderBriefResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse
import com.ChickenKitchen.app.model.dto.response.OrderTrackingResponse
import com.ChickenKitchen.app.model.entity.order.Feedback
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.order.OrderStep
import com.ChickenKitchen.app.model.entity.order.OrderStepItem
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.repository.order.FeedbackRepository
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.order.OrderStepItemRepository
import com.ChickenKitchen.app.repository.order.OrderStepRepository
import com.ChickenKitchen.app.repository.order.OrderDishRepository
import com.ChickenKitchen.app.repository.payment.PaymentRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.repository.step.StepRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.repository.promotion.OrderPromotionRepository
import com.ChickenKitchen.app.service.notification.NotificationService
import com.ChickenKitchen.app.service.order.CustomerOrderService
import com.ChickenKitchen.app.service.payment.PaymentService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDate

@Service
class CustomerOrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val feedbackRepository: FeedbackRepository,
    private val orderStepRepository: OrderStepRepository,
    private val orderStepItemRepository: OrderStepItemRepository,
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val stepRepository: StepRepository,
    private val menuItemRepository: MenuItemRepository,
    private val dishRepository: DishRepository,
    private val orderDishRepository: OrderDishRepository,
    private val paymentRepository: PaymentRepository,
    private val orderPromotionRepository: OrderPromotionRepository,

    private val paymentService: PaymentService,
    private val notificationService: NotificationService,
) : CustomerOrderService {

    private fun recalcAndPersistOrderTotal(order: Order) {
        val links = orderDishRepository.findAllByOrderId(order.id!!)
        val sum = links.sumOf { link -> link.dish.price * (link.quantity) }
        order.totalPrice = sum
        orderRepository.save(order)
    }

    private fun syncPendingPaymentAmounts(order: Order) {
        val existing = paymentRepository.findByOrderId(order.id!!)
        if (existing != null && existing.status == PaymentStatus.PENDING) {
            val discount = existing.discountAmount
            val amount = order.totalPrice
            val final = (amount - discount).coerceAtLeast(0)
            existing.amount = amount
            existing.finalAmount = final
            paymentRepository.save(existing)
        }
    }

    private fun currentUser() : com.ChickenKitchen.app.model.entity.user.User {
        val auth = SecurityContextHolder.getContext().authentication
        val email = when (val principal = auth?.principal) {
            is UserDetails -> principal.username
            is String -> if (principal.contains("@")) principal else auth.name
            else -> auth?.name
        }
        if (email.isNullOrBlank() || !email.contains("@")) {
            throw UserNotFoundException("Authenticated email not found in security context")
        }
        return userRepository.findByEmail(email)
            ?: throw UserNotFoundException("User not found with email: $email")
    }

    private fun ensureCurrentNewOrderFor(user: com.ChickenKitchen.app.model.entity.user.User, store: com.ChickenKitchen.app.model.entity.ingredient.Store): Order {
        val newOrders = orderRepository.findAllByUserEmailAndStoreIdAndStatusInOrderByCreatedAtDesc(
            user.email, store.id!!, listOf(OrderStatus.NEW)
        )
        return if (newOrders.isEmpty()) {
            orderRepository.save(
                Order(
                    user = user,
                    store = store,
                    totalPrice = 0,
                    status = OrderStatus.NEW,
                    pickupTime = Timestamp(System.currentTimeMillis()),
                )
            )
        } else {
            val keeper = newOrders.first()
            if (newOrders.size > 1) {
                newOrders.drop(1).forEach { dup ->
                    // Cleanup duplicate NEW orders fully then delete the order
                    orderStepItemRepository.deleteByOrderId(dup.id!!)
                    orderStepRepository.deleteByDishOrderId(dup.id!!)
                    dishRepository.deleteByOrderId(dup.id!!)
                    orderDishRepository.deleteByOrderId(dup.id!!)
                    orderRepository.delete(dup)
                }
            }
            return keeper
        }
    }

    @Transactional
    override fun addExistingDishToCurrentOrder(req: CreateExistingDishRequest): AddDishResponse {
        if (req.quantity <= 0) throw InvalidOrderStepException("Quantity must be greater than 0")

        val user = currentUser()
        val store = storeRepository.findById(req.storeId)
            .orElseThrow { StoreNotFoundException("Store with id ${req.storeId} not found") }

        val order = ensureCurrentNewOrderFor(user, store)

        val dish = dishRepository.findById(req.dishId)
            .orElseThrow { DishNotFoundException("Dish with id ${req.dishId} not found") }
        if (dish.isCustom) throw InvalidOrderStepException("Dish ${req.dishId} is custom; use custom endpoint instead")

        val existingLink = orderDishRepository.findByOrderIdAndDishId(order.id!!, dish.id!!)
        if (existingLink != null) {
            existingLink.quantity += req.quantity
            orderDishRepository.save(existingLink)
        } else {
            orderDishRepository.save(
                com.ChickenKitchen.app.model.entity.order.OrderDish(order = order, dish = dish, quantity = req.quantity)
            )
        }

        recalcAndPersistOrderTotal(order)
        syncPendingPaymentAmounts(order)

        return AddDishResponse(
            orderId = order.id!!,
            dishId = dish.id!!,
            status = order.status.name,
            createdSteps = emptyList()
        )
    }

    @Transactional
    override fun addCustomDishToCurrentOrder(req: CreateCustomDishRequest): AddDishResponse {
        if (!req.isCustom) throw InvalidOrderStepException("Custom dish must have isCustom=true")
        if (req.selections.isEmpty()) throw InvalidOrderStepException("Dish must contain at least one step selection")

        val user = currentUser()
        val store = storeRepository.findById(req.storeId)
            .orElseThrow { StoreNotFoundException("Store with id ${req.storeId} not found") }

        val order = ensureCurrentNewOrderFor(user, store)

        val dish = dishRepository.save(Dish(price = 0, cal = 0, isCustom = true, note = req.note))
        orderDishRepository.save(com.ChickenKitchen.app.model.entity.order.OrderDish(order = order, dish = dish))

        val stepMap = stepRepository.findAllById(req.selections.map { it.stepId }).associateBy { it.id!! }
        val createdSteps = mutableListOf<CreatedOrderStep>()

        var totalPrice = 0
        var totalCal = 0

        req.selections.sortedBy { stepMap[it.stepId]?.stepNumber ?: Int.MAX_VALUE }.forEach { sel ->
            val step = stepMap[sel.stepId] ?: throw StepNotFoundException("Step with id ${sel.stepId} not found")
            if (sel.items.isEmpty()) throw InvalidOrderStepException("Step ${step.id} must include at least one item")

            val orderStep = orderStepRepository.save(OrderStep(dish = dish, step = step))
            val createdItems = sel.items.map { item ->
                if (item.quantity <= 0) throw InvalidOrderStepException("Quantity must be greater than 0")
                val menuItem = menuItemRepository.findById(item.menuItemId)
                    .orElseThrow { MenuItemNotFoundException("Menu item with id ${item.menuItemId} not found") }
                if (menuItem.category.id != step.category.id)
                    throw InvalidOrderStepException("MenuItem ${menuItem.id} does not belong to step ${step.id} category")

                val osi = OrderStepItem(orderStep = orderStep, menuItem = menuItem, quantity = item.quantity)
                orderStep.items.add(osi)

                totalPrice += menuItem.price * item.quantity
                totalCal += menuItem.cal * item.quantity

                CreatedStepItem(menuItemId = menuItem.id!!, quantity = item.quantity)
            }

            orderStepRepository.save(orderStep)
            createdSteps.add(CreatedOrderStep(id = orderStep.id!!, stepId = step.id!!, items = createdItems))
        }

        dish.price = totalPrice
        dish.cal = totalCal
        dishRepository.save(dish)

        recalcAndPersistOrderTotal(order)
        syncPendingPaymentAmounts(order)

        return AddDishResponse(
            orderId = order.id!!,
            dishId = dish.id!!,
            status = order.status.name,
            createdSteps = createdSteps
        )
    }

    override fun getAllOrderStatuses(): List<OrderStatus> {
        return OrderStatus.values().toList()
    }

    @Transactional
    override fun createFeedback(orderId: Long, req: CreateFeedbackRequest): FeedbackResponse {
        val user = currentUser()
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.user.id != user.id) {
            throw IllegalArgumentException("You can only feedback your own order")
        }
        if (order.status != OrderStatus.COMPLETED) {
            throw InvalidOrderStatusException("Only COMPLETED orders can be feedbacked")
        }
        if (req.rating !in 1..5) {
            throw IllegalArgumentException("Rating must be between 1 and 5")
        }
        val existing = feedbackRepository.findByOrderId(orderId)
        if (existing != null) {
            throw IllegalArgumentException("Feedback already exists for this order")
        }

        val feedback = feedbackRepository.save(
            Feedback(
                order = order,
                store = order.store,
                rating = req.rating,
                message = req.message,
                reply = null
            )
        )

        order.feedback = feedback
        orderRepository.save(order)

        return FeedbackResponse(
            id = feedback.id!!,
            orderId = order.id!!,
            storeId = order.store.id!!,
            rating = feedback.rating,
            message = feedback.message,
            reply = feedback.reply,
            createdAt = feedback.createdAt,
            updatedAt = feedback.updatedAt
        )
    }

    override fun getFeedbackByOrder(orderId: Long): FeedbackResponse {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }
        val fb = feedbackRepository.findByOrderId(orderId)
            ?: throw IllegalArgumentException("Feedback not found for this order")

        return FeedbackResponse(
            id = fb.id!!,
            orderId = order.id!!,
            storeId = order.store.id!!,
            rating = fb.rating,
            message = fb.message,
            reply = fb.reply,
            createdAt = fb.createdAt,
            updatedAt = fb.updatedAt
        )
    }

    private fun progressFromStatus(status: OrderStatus): Int = when (status) {
        OrderStatus.NEW -> 10
        OrderStatus.FAILED -> 0
        OrderStatus.CONFIRMED -> 25
        OrderStatus.PROCESSING -> 60
        OrderStatus.READY -> 85
        OrderStatus.COMPLETED -> 100
        OrderStatus.CANCELLED -> 0
    }

    override fun getOrderTracking(orderId: Long): OrderTrackingResponse {
        val user = currentUser()
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.user.id != user.id) {
            throw IllegalArgumentException("You can only track your own order")
        }

        val allDishes = dishRepository.findAllByOrderId(order.id!!)
        val dishResponses = allDishes.map { d ->
            val steps = orderStepRepository.findAllByDishId(d.id!!)
            val stepResponses = steps.map { st ->
                val itemResponses = st.items.map { link ->
                    val mi = link.menuItem
                    CurrentStepItemResponse(
                        menuItemId = mi.id!!,
                        menuItemName = mi.name,
                        imageUrl = mi.imageUrl,
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
                name = d.name,
                isCustom = d.isCustom,
                note = d.note,
                price = d.price,
                cal = d.cal,
                updatedAt = d.updatedAt,
                steps = stepResponses
            )
        }

        return OrderTrackingResponse(
            orderId = order.id!!,
            status = order.status.name,
            progress = progressFromStatus(order.status),
            dishes = dishResponses
        )
    }

    @Transactional
    override fun getCurrentOrderForStore(storeId: Long): OrderCurrentResponse {
        val user = currentUser()
        val store =
            storeRepository.findById(storeId).orElseThrow { StoreNotFoundException("Store with id $storeId not found") }

        // Enforce single NEW order per user+store: keep latest, remove the rest
        val newOrders = orderRepository.findAllByUserEmailAndStoreIdAndStatusInOrderByCreatedAtDesc(
            user.email, store.id!!, listOf(OrderStatus.NEW)
        )
        val order = if (newOrders.isEmpty()) {
            orderRepository.save(
                Order(
                    user = user,
                    store = store,
                    totalPrice = 0,
                    status = OrderStatus.NEW,
                    pickupTime = Timestamp(System.currentTimeMillis()),
                )
            ).also { created ->
                return OrderCurrentResponse(
                    orderId = created.id!!,
                    status = created.status.name,
                    cleared = false,
                    dishes = emptyList()
                )
            }
        } else {
            val keeper = newOrders.first()
            if (newOrders.size > 1) {
                newOrders.drop(1).forEach { dup ->
                    orderStepItemRepository.deleteByOrderId(dup.id!!)
                    orderStepRepository.deleteByDishOrderId(dup.id!!)
                    dishRepository.deleteByOrderId(dup.id!!)
                    orderRepository.delete(dup)
                }
            }
            keeper
        }

        // Check dishes' updatedAt: if any dish is not updated today -> clear all dishes; else return all
        val today = LocalDate.now()
        val start = Timestamp.valueOf(today.atStartOfDay())
        val end = Timestamp.valueOf(today.atTime(23, 59, 59))

        val allDishes = dishRepository.findAllByOrderId(order.id!!)
        if (allDishes.isNotEmpty()) {
            val customDishes = allDishes.filter { it.isCustom }
            val allUpdatedToday = if (customDishes.isEmpty()) true else customDishes.all { d ->
                val u = d.updatedAt
                u != null && !u.before(start) && !u.after(end)
            }
            if (!allUpdatedToday) {
                orderStepItemRepository.deleteByOrderId(order.id!!)
                orderStepRepository.deleteByDishOrderId(order.id!!)
                dishRepository.deleteByOrderId(order.id!!)
                orderDishRepository.deleteByOrderId(order.id!!)
                // Reset order total when clearing dishes
                order.totalPrice = 0
                orderRepository.save(order)
                return OrderCurrentResponse(order.id!!, order.status.name, true, emptyList())
            }
        }

        val dishResponses = allDishes.map { d ->
            val steps = orderStepRepository.findAllByDishId(d.id!!)
            val stepResponses = steps.map { st ->
                val itemResponses = st.items.map { link ->
                    val mi = link.menuItem
                    CurrentStepItemResponse(
                        menuItemId = mi.id!!,
                        menuItemName = mi.name,
                        imageUrl = mi.imageUrl,
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
                name = d.name,
                isCustom = d.isCustom,
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
        val user = currentUser()
        val store =
            storeRepository.findById(storeId).orElseThrow { StoreNotFoundException("Store with id $storeId not found") }

        val statuses = listOf(OrderStatus.CONFIRMED, OrderStatus.COMPLETED, OrderStatus.CANCELLED, OrderStatus.PROCESSING)
        val list = orderRepository.findAllByUserEmailAndStoreIdAndStatusInOrderByCreatedAtDesc(
            user.email,
            store.id!!,
            statuses
        )
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
    override fun updateDish(dishId: Long, req: UpdateDishRequest): AddDishResponse {
        if (req.selections.isEmpty()) throw InvalidOrderStepException("Dish must contain at least one step selection")

        val dish = dishRepository.findById(dishId)
            .orElseThrow { DishNotFoundException("Dish with id $dishId not found") }
        val orderId = orderDishRepository.findOrderIdByDishId(dishId)
            ?: throw OrderNotFoundException("Order for dish $dishId not found")
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.status != OrderStatus.NEW)
            throw InvalidOrderStatusException("Cannot update dish when order is ${order.status}")

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
            val step = stepMap[sel.stepId] ?: throw StepNotFoundException("Step with id ${sel.stepId} not found")
            if (sel.items.isEmpty()) throw InvalidOrderStepException("Step ${step.id} must include at least one item")

            val orderStep = orderStepRepository.save(
                OrderStep(
                    dish = dish,
                    step = step
                )
            )

            val createdItems = mutableListOf<CreatedStepItem>()
            sel.items.forEach { item ->
                if (item.quantity <= 0) throw InvalidOrderStepException("Quantity must be greater than 0")

                val menuItem = menuItemRepository.findById(item.menuItemId)
                    .orElseThrow { MenuItemNotFoundException("Menu item with id ${item.menuItemId} not found") }

                if (menuItem.category.id != step.category.id)
                    throw InvalidOrderStepException("MenuItem ${menuItem.id} does not belong to step ${step.id} category")

                val link = com.ChickenKitchen.app.model.entity.order.OrderStepItem(
                    orderStep = orderStep,
                    menuItem = menuItem,
                    quantity = item.quantity
                )
                orderStep.items.add(link)
                createdItems.add(CreatedStepItem(menuItemId = menuItem.id!!, quantity = item.quantity))

                totalPrice += (menuItem.price * item.quantity)
                totalCal += (menuItem.cal * item.quantity)
            }
            orderStepRepository.save(orderStep)
            created.add(CreatedOrderStep(id = orderStep.id!!, stepId = step.id!!, items = createdItems))
        }

        // Update dish note/price/cal
        dish.price = totalPrice
        dish.cal = totalCal
        dishRepository.save(dish)

        // Recalculate and persist order total since dish changed
        recalcAndPersistOrderTotal(order)
        // If there is a pending payment, sync its amount to new order total
        syncPendingPaymentAmounts(order)

        return AddDishResponse(
            orderId = order.id!!,
            dishId = dish.id!!,
            status = order.status.name,
            createdSteps = created
        )
    }

    @Transactional
    override fun deleteDish(dishId: Long): Long {
        val dish = dishRepository.findById(dishId)
            .orElseThrow { DishNotFoundException("Dish with id $dishId not found") }
        val orderId = orderDishRepository.findOrderIdByDishId(dishId)
            ?: throw OrderNotFoundException("Order for dish $dishId not found")
        try {
            orderStepItemRepository.deleteByDishId(dishId)
            orderStepRepository.deleteByDishId(dishId)
            // Remove link and dish entity
            orderDishRepository.deleteByDishId(dishId)
            dishRepository.delete(dish)
        } catch (ex: Exception) {
            throw DeleteDishFailedException("Failed to delete dish with id $dishId: ${ex.message}")
        }
        // Recalculate order total after deletion
        val order = orderRepository.findById(orderId).orElseThrow { OrderNotFoundException("Order with id $orderId not found") }
        recalcAndPersistOrderTotal(order)
        return orderId
    }

    @Transactional
    override fun customerCancelOrder(orderId: Long, reason: String?): OrderTrackingResponse? {
        val user = currentUser()
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.user.id != user.id) {
            throw OrderNotFoundException("You can only cancel your own order")
        }

        return when (order.status) {
            OrderStatus.NEW -> handleNewOrderCancellation(order)
            OrderStatus.FAILED, OrderStatus.PROCESSING, OrderStatus.CONFIRMED ->
                handleRefundableCancellation(order, user, reason)
            OrderStatus.READY -> handleReadyOrderCancellation(order, reason)
            else -> getOrderTracking(order.id!!)
        }
    }

    private fun handleNewOrderCancellation(order: Order): OrderTrackingResponse? {
        val orderId = order.id ?: throw IllegalArgumentException("Order ID cannot be null")

        orderStepItemRepository.deleteByOrderId(orderId)
        orderStepRepository.deleteByDishOrderId(orderId)
        orderDishRepository.deleteByOrderId(orderId)

        val orderDishes = orderDishRepository.findByOrderId(orderId)
        orderDishes.map { it.dish }
            .filter { !it.isCustom }
            .forEach { dish ->
                if (orderDishRepository.countByDishId(dish.id!!) == 0L) {
                    dishRepository.delete(dish)
                }
            }


        paymentRepository.findByOrderId(orderId)?.let { payment ->
            if (payment.status == PaymentStatus.PENDING) {
                paymentRepository.delete(payment)
            }
        }

        orderPromotionRepository.deleteByOrderId(orderId)
        orderRepository.delete(order)

        return null
    }

    private fun handleRefundableCancellation(order: Order, user: User, reason: String?): OrderTrackingResponse {
        order.status = OrderStatus.CANCELLED
        orderRepository.save(order)

        paymentService.refundPayment(order, reason)

        notificationService.sendToUser(
            SingleNotificationRequest(
                user = user,
                title = "Order Cancelled and Refunded",
                body = "Your order ${order.id} has been cancelled and a refund has been credited to your wallet."
            )
        )

        return getOrderTracking(order.id!!)
    }

    private fun handleReadyOrderCancellation(order: Order, reason: String?): OrderTrackingResponse {
        order.status = OrderStatus.CANCELLED
        orderRepository.save(order)

        paymentService.rejectRefund(order, reason)

        notificationService.sendToUser(
            SingleNotificationRequest(
                user = order.user,
                title = "Order Cancelled",
                body = "Your order ${order.id} has been cancelled. Since it was already being prepared, no refund will be issued."
            )
        )

        return getOrderTracking(order.id!!)
    }
}
