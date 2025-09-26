package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enum.OrderStatus
import com.ChickenKitchen.app.enum.MenuType
import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.handler.QuantityMustBeNonNegativeException
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.handler.AccessDeniedException
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
import com.ChickenKitchen.app.repository.recipe.RecipeRepository
import com.ChickenKitchen.app.repository.combo.ComboRepository
import com.ChickenKitchen.app.service.order.OrderService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDate
import org.springframework.security.core.context.SecurityContextHolder

import com.ChickenKitchen.app.model.dto.request.AddOrderItemRequest
import com.ChickenKitchen.app.model.dto.response.UserOrderDetailResponse
import com.ChickenKitchen.app.model.dto.response.UserOrderResponse
import com.ChickenKitchen.app.model.dto.response.UserOrderItemDetailResponse
import com.ChickenKitchen.app.model.dto.response.UserAddressResponse
import com.ChickenKitchen.app.model.dto.request.UpdateUserOrderItemRequest
import com.ChickenKitchen.app.model.dto.request.ConfirmOrderRequest
import com.ChickenKitchen.app.repository.user.UserAddressRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.promotion.PromotionRepository
import com.ChickenKitchen.app.repository.promotion.PromotionOrderRepository
import com.ChickenKitchen.app.repository.payment.TransactionRepository
import com.ChickenKitchen.app.enum.PaymentMethodType
import com.ChickenKitchen.app.enum.DiscountType
import com.ChickenKitchen.app.model.entity.promotion.PromotionOrder
import com.ChickenKitchen.app.model.entity.transaction.Transaction

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val statusHistoryRepository: OrderStatusHistoryRepository,
    private val userRepository: UserRepository,
    private val dailyMenuItemRepository: DailyMenuItemRepository,
    private val recipeRepository: RecipeRepository,
    private val comboRepository: ComboRepository,
    private val userAddressRepository: UserAddressRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val promotionRepository: PromotionRepository,
    private val promotionOrderRepository: PromotionOrderRepository,
    private val transactionRepository: TransactionRepository
) : OrderService {

    override fun getUserOrders(): List<UserOrderResponse> {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found")

        val orders = orderRepository.findAllByUserId(user.id!!)
        if (orders.isEmpty()) return emptyList()

        return orders.map { order ->
            val items = orderItemRepository.findAllByOrderId(order.id!!)
            UserOrderResponse(
                id = order.id!!,
                totalPrice = order.totalPrice,
                status = order.status,
                createdAt = order.createdAt,
                items = items.map { oi -> toUserOrderItemDetail(oi) },
                address = if (order.status != OrderStatus.NEW && order.userAddress != null)
                    UserAddressResponse(
                        id = order.userAddress!!.id!!,
                        recipientName = order.userAddress!!.recipientName,
                        phone = order.userAddress!!.phone,
                        addressLine = order.userAddress!!.addressLine,
                        city = order.userAddress!!.city,
                        isDefault = order.userAddress!!.isDefault
                    ) else null
            )
        }
    }

    override fun getUserOrderById(id: Long): UserOrderDetailResponse {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found")

        val order = orderRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Order with id $id not found")

        if (order.user.id != user.id) {
            throw AccessDeniedException("You do not have access to this order")
        }

        val items = orderItemRepository.findAllByOrderId(order.id!!)
        return UserOrderDetailResponse(
            id = order.id!!,
            totalPrice = order.totalPrice,
            status = order.status,
            createdAt = order.createdAt,
            items = items.map { oi -> toUserOrderItemDetail(oi) },
            address = if (order.status != OrderStatus.NEW && order.userAddress != null)
                UserAddressResponse(
                    id = order.userAddress!!.id!!,
                    recipientName = order.userAddress!!.recipientName,
                    phone = order.userAddress!!.phone,
                    addressLine = order.userAddress!!.addressLine,
                    city = order.userAddress!!.city,
                    isDefault = order.userAddress!!.isDefault
                ) else null
        )
    }

    override fun addOrderItem(req: AddOrderItemRequest): UserOrderDetailResponse {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found")

        if (req.quantity < 0) throw QuantityMustBeNonNegativeException("Quantity must be non-negative")

        // Try to find an existing NEW order for the user; if none, create one
        val existingNew = orderRepository.findAllByUserUsernameAndStatus(username, OrderStatus.NEW)
        val order = if (existingNew.isNotEmpty()) existingNew.first() else {
            val newOrder = Order(
                user = user,
                totalPrice = BigDecimal.ZERO,
                status = OrderStatus.NEW,
                createdAt = Timestamp(System.currentTimeMillis())
            )
            orderRepository.save(newOrder)
        }

        val dmi = dailyMenuItemRepository.findById(req.dailyMenuItemId).orElse(null)
            ?: throw IngredientNotFoundException("DailyMenuItem with id ${req.dailyMenuItemId} not found")

        val linePrice = dmi.price.multiply(BigDecimal(req.quantity))
        val lineCal = dmi.cal * req.quantity

        // Append new order item
        val newItem = OrderItem(
            order = order,
            dailyMenuItem = dmi,
            quantity = req.quantity,
            price = linePrice,
            cal = lineCal,
            note = req.note
        )
        order.order_items.add(newItem)

        // Recalculate total
        val total = order.order_items.fold(BigDecimal.ZERO) { acc, it -> acc + it.price }
        order.totalPrice = total

        val saved = orderRepository.save(order)
        val items = orderItemRepository.findAllByOrderId(saved.id!!)
        return UserOrderDetailResponse(
            id = saved.id!!,
            totalPrice = saved.totalPrice,
            status = saved.status,
            createdAt = saved.createdAt,
            items = items.map { oi -> toUserOrderItemDetail(oi) },
            address = null
        )
    }

    

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

    override fun updateUserOrderItem(orderId: Long, dailyMenuItemId: Long, req: UpdateUserOrderItemRequest): UserOrderDetailResponse? {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found")

        val order = orderRepository.findById(orderId).orElse(null)
            ?: throw IngredientNotFoundException("Order with id $orderId not found")

        if (order.user.id != user.id) {
            throw AccessDeniedException("You do not have access to this order")
        }
        if (order.status != OrderStatus.NEW) {
            throw AccessDeniedException("Only NEW orders can be modified")
        }

        if (req.quantity < 0) throw QuantityMustBeNonNegativeException("Quantity must be non-negative")

        val items = orderItemRepository.findAllByOrderId(order.id!!).toMutableList()
        val target = items.find { it.dailyMenuItem.id == dailyMenuItemId }
            ?: throw IngredientNotFoundException("Order item with DailyMenuItem id $dailyMenuItemId not found in this order")

        if (req.quantity == 0) {
            orderItemRepository.delete(target)
            items.removeIf { it.id == target.id }
        } else {
            val dmi = target.dailyMenuItem
            target.quantity = req.quantity
            target.price = dmi.price.multiply(BigDecimal(req.quantity))
            target.cal = dmi.cal * req.quantity
            target.note = req.note
            orderItemRepository.save(target)
        }

        if (items.isEmpty()) {
            orderRepository.delete(order)
            return null
        }

        val total = items.fold(BigDecimal.ZERO) { acc, it -> acc + it.price }
        order.totalPrice = total
        val saved = orderRepository.save(order)
        val refreshed = orderItemRepository.findAllByOrderId(saved.id!!)
        return UserOrderDetailResponse(
            id = saved.id!!,
            totalPrice = saved.totalPrice,
            status = saved.status,
            createdAt = saved.createdAt,
            items = refreshed.map { oi -> toUserOrderItemDetail(oi) },
            address = if (saved.status != OrderStatus.NEW && saved.userAddress != null)
                UserAddressResponse(
                    id = saved.userAddress!!.id!!,
                    recipientName = saved.userAddress!!.recipientName,
                    phone = saved.userAddress!!.phone,
                    addressLine = saved.userAddress!!.addressLine,
                    city = saved.userAddress!!.city,
                    isDefault = saved.userAddress!!.isDefault
                ) else null
        )
    }

    override fun deleteUserOrderItem(orderId: Long, dailyMenuItemId: Long) {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found")

        val order = orderRepository.findById(orderId).orElse(null)
            ?: throw IngredientNotFoundException("Order with id $orderId not found")

        if (order.user.id != user.id) {
            throw AccessDeniedException("You do not have access to this order")
        }
        if (order.status != OrderStatus.NEW) {
            throw AccessDeniedException("Only NEW orders can be modified")
        }

        val items = orderItemRepository.findAllByOrderId(order.id!!).toMutableList()
        val target = items.find { it.dailyMenuItem.id == dailyMenuItemId }
            ?: throw IngredientNotFoundException("Order item with DailyMenuItem id $dailyMenuItemId not found in this order")
        orderItemRepository.delete(target)
        items.removeIf { it.id == target.id }

        if (items.isEmpty()) {
            orderRepository.delete(order)
            return
        }

        val total = items.fold(BigDecimal.ZERO) { acc, it -> acc + it.price }
        order.totalPrice = total
        orderRepository.save(order)
    }

    override fun confirmUserOrder(orderId: Long, req: ConfirmOrderRequest): UserOrderDetailResponse {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found")

        val order = orderRepository.findById(orderId).orElse(null)
            ?: throw IngredientNotFoundException("Order with id $orderId not found")

        if (order.user.id != user.id) {
            throw AccessDeniedException("You do not have access to this order")
        }
        if (order.order_items.isEmpty()) {
            throw QuantityMustBeNonNegativeException("Order has no items to confirm")
        }
        if (order.status != OrderStatus.NEW) {
            throw AccessDeniedException("Only NEW orders can be confirmed by user")
        }

        // Validate and set user address
        val address = userAddressRepository.findById(req.userAddressId).orElse(null)
            ?: throw IngredientNotFoundException("UserAddress with id ${req.userAddressId} not found")
        if (address.user.id != user.id) {
            throw AccessDeniedException("Address does not belong to current user")
        }
        order.userAddress = address

        // Resolve payment method (default COD)
        val pmType = req.paymentMethod
        val paymentMethod = paymentMethodRepository.findByName(pmType).orElse(null)
            ?: throw IngredientNotFoundException("Payment method ${pmType.name} not found")

        // Apply promotion if provided
        req.promotionId?.let { pid ->
            val promo = promotionRepository.findById(pid).orElse(null)
                ?: throw IngredientNotFoundException("Promotion with id $pid not found")
            if (!promo.isActive) throw AccessDeniedException("Promotion is not active")

            val now = Timestamp(System.currentTimeMillis())
            if (now.before(promo.startDate) || now.after(promo.endDate)) {
                throw AccessDeniedException("Promotion is not valid at this time")
            }
            if (promo.quantity <= 0) throw AccessDeniedException("Promotion is out of quantity")

            val original = order.totalPrice
            val discounted = when (promo.discountType) {
                DiscountType.PERCENT -> {
                    val percent = promo.discountValue
                    original.subtract(original.multiply(percent).divide(BigDecimal(100)))
                }
                DiscountType.AMOUNT -> {
                    val amt = promo.discountValue
                    if (original <= amt) BigDecimal.ZERO else original.subtract(amt)
                }
            }
            order.totalPrice = discounted

            // Create PromotionOrder linkage and reduce promo quantity
            val po = PromotionOrder(order = order, promotion = promo, user = user, usedDate = now)
            promotionOrderRepository.save(po)
            promo.quantity = promo.quantity - 1
            promotionRepository.save(promo)
        }

        // Create a transaction record for the order
        val tx = Transaction(
            order = order,
            user = user,
            paymentMethod = paymentMethod,
            transactionType = com.ChickenKitchen.app.enum.TransactionType.CAPTURE,
            amount = order.totalPrice,
            createdAt = Timestamp(System.currentTimeMillis()),
            note = "COD"
        )
        transactionRepository.save(tx)

        // Update order status to CONFIRMED and add status history
        order.status = OrderStatus.CONFIRMED
        addStatusHistory(order, OrderStatus.CONFIRMED, note = "Order confirmed by user")

        val saved = orderRepository.save(order)
        val items = orderItemRepository.findAllByOrderId(saved.id!!)
        return UserOrderDetailResponse(
            id = saved.id!!,
            totalPrice = saved.totalPrice,
            status = saved.status,
            createdAt = saved.createdAt,
            items = items.map { oi -> toUserOrderItemDetail(oi) },
            address = if (saved.status != OrderStatus.NEW && saved.userAddress != null)
                UserAddressResponse(
                    id = saved.userAddress!!.id!!,
                    recipientName = saved.userAddress!!.recipientName,
                    phone = saved.userAddress!!.phone,
                    addressLine = saved.userAddress!!.addressLine,
                    city = saved.userAddress!!.city,
                    isDefault = saved.userAddress!!.isDefault
                ) else null
        )
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
        OrderStatus.NEW -> OrderStatus.CONFIRMED
        OrderStatus.CONFIRMED -> OrderStatus.PREPARING
        OrderStatus.PREPARING -> OrderStatus.READY
        OrderStatus.READY -> OrderStatus.DELIVERING
        OrderStatus.DELIVERING -> OrderStatus.COMPLETED
        OrderStatus.COMPLETED -> OrderStatus.COMPLETED
        OrderStatus.CANCELED -> OrderStatus.CANCELED
    }

    private fun toUserOrderItemDetail(oi: OrderItem): UserOrderItemDetailResponse {
        val dmi = oi.dailyMenuItem

        var name = ""
        var imageUrl = ""

        when (dmi.menuType) {
            MenuType.MEAL -> {
                val recipe = recipeRepository.findById(dmi.refId).orElse(null)
                if (recipe != null) {
                    name = recipe.name
                    imageUrl = recipe.image ?: ""
                } else {
                    name = "Meal"
                }
            }
            MenuType.COMBO -> {
                val combo = comboRepository.findById(dmi.refId).orElse(null)
                name = combo?.name ?: "Combo"
                imageUrl = ""
            }
            MenuType.EXTRA -> {
                name = "Extra"
                imageUrl = ""
            }
        }

        return UserOrderItemDetailResponse(
            dailyMenuItemId = dmi.id!!,
            name = name,
            imageUrl = imageUrl,
            quantity = oi.quantity,
            price = oi.price,
            cal = oi.cal,
            note = oi.note
        )
    }
}
