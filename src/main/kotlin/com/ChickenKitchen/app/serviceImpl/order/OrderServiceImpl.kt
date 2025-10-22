package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enums.DiscountType
import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.enums.PaymentStatus
import com.ChickenKitchen.app.handler.DailyMenuUnavailableException
import com.ChickenKitchen.app.handler.DeleteDishFailedException
import com.ChickenKitchen.app.handler.DishNotFoundException
import com.ChickenKitchen.app.handler.InvalidOrderStatusException
import com.ChickenKitchen.app.handler.InvalidOrderStepException
import com.ChickenKitchen.app.handler.MenuItemNotFoundException
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.handler.PaymentMethodNameNotAvailable
import com.ChickenKitchen.app.handler.PaymentMethodNotFoundException
import com.ChickenKitchen.app.handler.PromotionNotFoundException
import com.ChickenKitchen.app.handler.PromotionNotValidThisTime
import com.ChickenKitchen.app.handler.StepNotFoundException
import com.ChickenKitchen.app.handler.StoreNotFoundException
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.model.dto.request.CreateDishRequest
import com.ChickenKitchen.app.model.dto.request.OrderConfirmRequest
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
import com.ChickenKitchen.app.model.entity.order.OrderStepItem
import com.ChickenKitchen.app.model.entity.payment.Payment
import com.ChickenKitchen.app.model.entity.promotion.OrderPromotion
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuRepository
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.order.OrderStepRepository
import com.ChickenKitchen.app.repository.order.OrderStepItemRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.payment.PaymentRepository
import com.ChickenKitchen.app.repository.promotion.OrderPromotionRepository
import com.ChickenKitchen.app.repository.promotion.PromotionRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.repository.step.StepRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.order.OrderService
import com.ChickenKitchen.app.service.payment.VNPayService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderStepRepository: OrderStepRepository,
    private val orderStepItemRepository: OrderStepItemRepository,
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
    private val stepRepository: StepRepository,
    private val menuItemRepository: MenuItemRepository,
    private val dailyMenuRepository: DailyMenuRepository,
    private val dishRepository: DishRepository,
    private val promotionRepository: PromotionRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val orderPromotionRepository: OrderPromotionRepository,
    private val paymentRepository: PaymentRepository,

    private val vnPayService: VNPayService
) : OrderService {

    @Transactional
    override fun addDishToCurrentOrder(req: CreateDishRequest): AddDishResponse {
        if (req.selections.isEmpty()) throw InvalidOrderStepException("Dish must contain at least one step selection")

        val principal = SecurityContextHolder.getContext().authentication
        val email = principal?.name
        val effectiveEmail = if (email?.contains("@") == true) email else "chickenkitchen785@gmail.com"
        val user = userRepository.findByEmail(effectiveEmail)
            ?: userRepository.findAll().firstOrNull()
            ?: throw UserNotFoundException("User not found")

        val store = storeRepository.findById(req.storeId)
            .orElseThrow { StoreNotFoundException("Store with id ${req.storeId} not found") }

        // Find current NEW order or create one
        val order = orderRepository.findFirstByUserEmailAndStoreIdAndStatusOrderByCreatedAtDesc(
            user.email, store.id!!, OrderStatus.NEW
        ) ?: orderRepository.save(
            Order(
                user = user,
                store = store,
                totalPrice = 0,
                status = OrderStatus.NEW,
                pickupTime = Timestamp(System.currentTimeMillis())
            )
        )

        val existingPayment = paymentRepository.findByOrderId(order.id!!)
        if (existingPayment != null) {
            throw InvalidOrderStepException("This order already has a payment and cannot be modified")
        }

        // Create dish entry with temporary totals = 0
        val dish = dishRepository.save(Dish(order = order, price = 0, cal = 0, note = req.note))

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

                val today = java.time.LocalDate.now()
                val start = Timestamp.valueOf(today.atStartOfDay())
                val end = Timestamp.valueOf(today.atTime(23, 59, 59))
                val todaysMenu = dailyMenuRepository.findByStoreAndDateRange(store.id!!, start, end)
                    ?: throw DailyMenuUnavailableException("No daily menu available for store ${store.id} today")
                val dmi = todaysMenu.dailyMenuItems.firstOrNull { it.menuItem.id == item.menuItemId }
                    ?: throw DailyMenuUnavailableException("Menu item ${item.menuItemId} not in today's daily menu")

                val osi = OrderStepItem(orderStep = orderStep, dailyMenuItem = dmi, quantity = item.quantity)
                orderStep.items.add(osi)

                totalPrice += menuItem.price * item.quantity
                totalCal += menuItem.cal * item.quantity

                CreatedStepItem(dailyMenuItemId = dmi.id!!, quantity = item.quantity)
            }

            orderStepRepository.save(orderStep)
            createdSteps.add(CreatedOrderStep(id = orderStep.id!!, stepId = step.id!!, items = createdItems))
        }

        // Update dish totals
        dish.price = totalPrice
        dish.cal = totalCal
        dishRepository.save(dish)

        // Gán tổng giá của dish vào order và lưu
        order.totalPrice += totalPrice
        orderRepository.save(order)

        return AddDishResponse(
            orderId = order.id!!,
            dishId = dish.id!!,
            status = order.status.name,
            createdSteps = createdSteps
        )
    }

    @Transactional
    override fun getCurrentOrderForStore(storeId: Long): OrderCurrentResponse {
        val principal = SecurityContextHolder.getContext().authentication
        val email = principal?.name
        val effectiveEmail = if (email != null && email.contains("@")) email else "chickenkitchen785@gmail.com"
        val user = userRepository.findByEmail(effectiveEmail)
            ?: userRepository.findAll().firstOrNull()
            ?: throw UserNotFoundException("User not found")
        val store =
            storeRepository.findById(storeId).orElseThrow { StoreNotFoundException("Store with id $storeId not found") }

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
            ?: throw UserNotFoundException("User not found")
        val store =
            storeRepository.findById(storeId).orElseThrow { StoreNotFoundException("Store with id $storeId not found") }

        val statuses = listOf(OrderStatus.COMPLETED, OrderStatus.CANCELLED, OrderStatus.PROCESSING)
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
    override fun updateDish(
        dishId: Long,
        req: com.ChickenKitchen.app.model.dto.request.UpdateDishRequest
    ): AddDishResponse {
        if (req.selections.isEmpty()) throw InvalidOrderStepException("Dish must contain at least one step selection")

        val dish =
            dishRepository.findById(dishId).orElseThrow { DishNotFoundException("Dish with id $dishId not found") }
        val order = dish.order

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

                val today = java.time.LocalDate.now()
                val start = java.sql.Timestamp.valueOf(today.atStartOfDay())
                val end = java.sql.Timestamp.valueOf(today.atTime(23, 59, 59))
                val todaysMenu = dailyMenuRepository.findByStoreAndDateRange(store.id!!, start, end)
                    ?: throw DailyMenuUnavailableException("No daily menu for store ${store.id} today")

                val dmi = todaysMenu.dailyMenuItems.firstOrNull { it.menuItem.id == item.menuItemId }
                    ?: throw DailyMenuUnavailableException("Menu item ${item.menuItemId} not in today's daily menu")

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
        val dish = dishRepository.findById(dishId)
            .orElseThrow { DishNotFoundException("Dish with id $dishId not found") }

        val orderId = dish.order.id!!
        try {
            orderStepItemRepository.deleteByDishId(dishId)
            orderStepRepository.deleteByDishId(dishId)
            dishRepository.delete(dish)
        } catch (ex: Exception) {
            throw DeleteDishFailedException("Failed to delete dish with id $dishId: ${ex.message}")
        }
        return orderId
    }

    @Transactional
    override fun confirmedOrder(req: OrderConfirmRequest): String {

        val order = orderRepository.findById(req.orderId)
            .orElseThrow { OrderNotFoundException("Order with id ${req.orderId} not found") }

        if (order.status != OrderStatus.NEW && order.status != OrderStatus.FAILED) {
            throw InvalidOrderStatusException("Order is not in a valid state to confirm payment (must be NEW or FAILED)")
        }

        val paymentMethod = paymentMethodRepository.findById(req.paymentMethodId)
            .orElseThrow { PaymentMethodNotFoundException("Payment Method with id ${req.paymentMethodId} not found") }

        if (!paymentMethod.isActive) {
            throw PaymentMethodNameNotAvailable("Payment method ${paymentMethod.name} is not available")
        }

        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication?.name
        val effectiveEmail = if (email != null && email.contains("@")) email else "chickenkitchen785@gmail.com"

        val user = userRepository.findByEmail(effectiveEmail)
            ?: throw UserNotFoundException("User not found with email: $effectiveEmail")


        var discountAmount = 0
        var finalAmount = order.totalPrice
        req.promotionId?.let { promotionId ->
            val promo = promotionRepository.findById(promotionId)
                .orElseThrow { PromotionNotFoundException("Promotion with id $promotionId not found") }

            val now = LocalDateTime.now()
            if (now.isBefore(promo.startDate) || now.isAfter(promo.endDate)) {
                throw PromotionNotValidThisTime("Promotion is not valid at this time")
            }

            if (promo.quantity <= 0) {
                throw PromotionNotValidThisTime("Promotion is out of quantity")
            }

            discountAmount = if (promo.discountType == DiscountType.PERCENT) {
                (order.totalPrice * promo.discountValue) / 100
            } else {
                promo.discountValue
            }

            finalAmount = order.totalPrice - discountAmount

            // Lưu OrderPromotion và giảm quantity
            orderPromotionRepository.save(
                OrderPromotion(order = order, promotion = promo, user = user, usedDate = now)
            )
            promo.quantity -= 1
            promotionRepository.save(promo)
        }


        val existingPayment = paymentRepository.findByOrderId(order.id!!)

        val payment = if (existingPayment != null) {
            if (order.status == OrderStatus.FAILED) {
                // Cập nhật lại payment từ FAILED thành PENDING để retry
                existingPayment.status = PaymentStatus.PENDING
                paymentRepository.save(existingPayment)
                existingPayment
            } else {
                throw InvalidOrderStepException("This order already has a payment and cannot be confirmed again.")
            }
        } else {
            // Tạo payment mới nếu chưa có
            Payment(
                user = user,
                order = order,
                amount = order.totalPrice,
                discountAmount = discountAmount,
                finalAmount = finalAmount,
                status = PaymentStatus.PENDING
            ).also { paymentRepository.save(it) }
        }

        // 6️⃣ Xử lý theo loại PaymentMethod
        return when (paymentMethod.name.uppercase()) {
            "VNPAY" -> vnPayService.createVnPayURL(order.id!!)
            else -> throw PaymentMethodNameNotAvailable("Payment method ${paymentMethod.name} is not supported yet")
        }
    }
}
