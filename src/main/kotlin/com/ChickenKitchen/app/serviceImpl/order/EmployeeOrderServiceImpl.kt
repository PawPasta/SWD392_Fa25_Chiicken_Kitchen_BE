package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.handler.InvalidOrderStatusException
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.handler.StoreNotFoundException
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.model.dto.response.CurrentDishResponse
import com.ChickenKitchen.app.model.dto.response.CurrentStepItemResponse
import com.ChickenKitchen.app.model.dto.response.CurrentStepResponse
import com.ChickenKitchen.app.model.dto.response.EmployeeOrderListItemResponse
import com.ChickenKitchen.app.model.dto.response.OrderCustomerResponse
import com.ChickenKitchen.app.model.dto.response.OrderBriefResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse
import com.ChickenKitchen.app.model.dto.response.EmployeeOrderDetailResponse
import com.ChickenKitchen.app.model.dto.response.DishWithIngredientsResponse
import com.ChickenKitchen.app.model.dto.response.StepItemWithIngredientsResponse
import com.ChickenKitchen.app.model.dto.response.StepWithIngredientsResponse
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.order.OrderDishRepository
import com.ChickenKitchen.app.repository.order.OrderStepRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.repository.user.EmployeeDetailRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.order.EmployeeOrderService
import com.ChickenKitchen.app.service.payment.PaymentService
import com.ChickenKitchen.app.mapper.toEmployeeDetailFullResponse
import com.ChickenKitchen.app.mapper.toIngredientResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmployeeOrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderStepRepository: OrderStepRepository,
    private val dishRepository: DishRepository,
    private val userRepository: UserRepository,
    private val employeeDetailRepository: EmployeeDetailRepository,
    private val orderDishRepository: OrderDishRepository,

    private val paymentService: PaymentService,
) : EmployeeOrderService {

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

    // Removed legacy confirmed list; superseded by pageable status endpoint

    override fun getConfirmedOrderDetailForEmployee(orderId: Long): OrderCurrentResponse {
        val user = currentUser()
        val detail = employeeDetailRepository.findByUser(user)
            ?: throw UserNotFoundException("Employee detail not found for user ${user.email}")

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.status != OrderStatus.CONFIRMED) {
            throw InvalidOrderStatusException("Order is not CONFIRMED")
        }

        if (order.store.id != detail.store.id) {
            throw StoreNotFoundException("Order does not belong to employee's store")
        }

        val dishes = dishRepository.findAllByOrderId(order.id!!)
        val qtyMap = orderDishRepository.findAllByOrderId(order.id!!).associateBy({ it.dish.id!! }, { it.quantity })
        val dishResponses = dishes.map { d ->
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
                quantity = qtyMap[d.id!!] ?: 1,
                isCustom = d.isCustom,
                note = d.note,
                price = d.price,
                cal = d.cal,
                updatedAt = d.updatedAt,
                steps = stepResponses
            )
        }

        return OrderCurrentResponse(
            orderId = order.id!!,
            status = order.status.name,
            cleared = false,
            dishes = dishResponses
        )
    }

    override fun employeeAcceptOrder(orderId: Long): OrderBriefResponse {
        val user = currentUser()
        val detail = employeeDetailRepository.findByUser(user)
            ?: throw UserNotFoundException("Employee detail not found for user ${user.email}")

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.store.id != detail.store.id) {
            throw StoreNotFoundException("Order does not belong to employee's store")
        }

        if (order.status != OrderStatus.CONFIRMED) {
            throw InvalidOrderStatusException("Only CONFIRMED orders can be accepted")
        }

        order.status = OrderStatus.PROCESSING
        orderRepository.save(order)

        return OrderBriefResponse(
            orderId = order.id!!,
            storeId = order.store.id!!,
            status = order.status.name,
            totalPrice = order.totalPrice,
            createdAt = order.createdAt,
            pickupTime = order.pickupTime
        )
    }

    override fun employeeMarkReadyOrder(orderId: Long): OrderBriefResponse {
        val user = currentUser()
        val detail = employeeDetailRepository.findByUser(user)
            ?: throw UserNotFoundException("Employee detail not found for user ${user.email}")

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.store.id != detail.store.id) {
            throw StoreNotFoundException("Order does not belong to employee's store")
        }

        if (order.status != OrderStatus.PROCESSING) {
            throw InvalidOrderStatusException("Only PROCESSING orders can be marked READY")
        }

        order.status = OrderStatus.READY
        orderRepository.save(order)

        return OrderBriefResponse(
            orderId = order.id!!,
            storeId = order.store.id!!,
            status = order.status.name,
            totalPrice = order.totalPrice,
            createdAt = order.createdAt,
            pickupTime = order.pickupTime
        )
    }

    override fun employeeCompleteOrder(orderId: Long): OrderBriefResponse {
        val user = currentUser()
        val detail = employeeDetailRepository.findByUser(user)
            ?: throw UserNotFoundException("Employee detail not found for user ${user.email}")

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.store.id != detail.store.id) {
            throw StoreNotFoundException("Order does not belong to employee's store")
        }

        if (order.status != OrderStatus.READY) {
            throw InvalidOrderStatusException("Only READY orders can be completed")
        }

        order.status = OrderStatus.COMPLETED
        orderRepository.save(order)

        return OrderBriefResponse(
            orderId = order.id!!,
            storeId = order.store.id!!,
            status = order.status.name,
            totalPrice = order.totalPrice,
            createdAt = order.createdAt,
            pickupTime = order.pickupTime
        )
    }

    @Transactional
    override fun employeeCancelOrder(orderId: Long): OrderBriefResponse {
        val employee = currentUser()
        val detail = employeeDetailRepository.findByUser(employee)
            ?: throw UserNotFoundException("Employee detail not found for user ${employee.email}")

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.store.id != detail.store.id) {
            throw StoreNotFoundException("Order does not belong to employee's store")
        }

        when (order.status) {
            OrderStatus.CONFIRMED, OrderStatus.PROCESSING -> {
                // ✅ Hoàn tiền và huỷ đơn
                paymentService.refundPayment(order, "Cancelled by employee")
                order.status = OrderStatus.CANCELLED
                orderRepository.save(order)
            }

            OrderStatus.READY, OrderStatus.COMPLETED -> {
                throw InvalidOrderStatusException("Employee cannot cancel READY or COMPLETED orders")
            }

            else -> {
                throw InvalidOrderStatusException("Order cannot be cancelled in status ${order.status}")
            }
        }

        return OrderBriefResponse(
            orderId = order.id!!,
            storeId = order.store.id!!,
            status = order.status.name,
            totalPrice = order.totalPrice,
            createdAt = order.createdAt,
            pickupTime = order.pickupTime
        )
    }

    @Transactional(readOnly = true)
    override fun getMyEmployeeDetail(): com.ChickenKitchen.app.model.dto.response.EmployeeDetailFullResponse {
        val user = currentUser()
        val detail = employeeDetailRepository.findByUser(user)
            ?: throw UserNotFoundException("Employee detail not found for user ${user.email}")
        return detail.toEmployeeDetailFullResponse()
    }

    @Transactional(readOnly = true)
    override fun getOrdersForEmployeeStore(
        status: String?,
        pageNumber: Int,
        size: Int,
        keyword: String?
    ): com.ChickenKitchen.app.model.dto.response.EmployeeOrderListPageResponse {
        val user = currentUser()
        val detail = employeeDetailRepository.findByUser(user)
            ?: throw UserNotFoundException("Employee detail not found for user ${user.email}")

        val storeId = detail.store.id ?: throw StoreNotFoundException("Store not found for employee ${user.email}")

        val safeSize = kotlin.math.max(size, 1)
        val safePage = kotlin.math.max(pageNumber, 1) - 1
        val pageable = org.springframework.data.domain.PageRequest.of(
            safePage,
            safeSize,
            org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt")
        )

        val page = if (status.isNullOrBlank()) {
            if (!keyword.isNullOrBlank())
                orderRepository.searchAllByStoreIdAndStatusNot(storeId, OrderStatus.NEW, keyword.trim(), pageable)
            else
                orderRepository.findAllByStoreIdAndStatusNot(storeId, OrderStatus.NEW, pageable)
        } else {
            val parsed = try {
                OrderStatus.valueOf(status.uppercase())
            } catch (_: Exception) {
                throw InvalidOrderStatusException("Invalid status: $status")
            }
            if (parsed == OrderStatus.NEW) {
                throw InvalidOrderStatusException("Fetching NEW orders is not allowed")
            }
            if (!keyword.isNullOrBlank())
                orderRepository.searchAllByStoreIdAndStatus(storeId, parsed, keyword.trim(), pageable)
            else
                orderRepository.findAllByStoreIdAndStatus(storeId, parsed, pageable)
        }

        val items = page.content.map { o ->
            val dishes = dishRepository.findAllByOrderId(o.id!!)
            val qtyMap = orderDishRepository.findAllByOrderId(o.id!!).associateBy({ it.dish.id!! }, { it.quantity })
            val dishResponses = dishes.map { d ->
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
                    quantity = qtyMap[d.id!!] ?: 1,
                    isCustom = d.isCustom,
                    note = d.note,
                    price = d.price,
                    cal = d.cal,
                    updatedAt = d.updatedAt,
                    steps = stepResponses
                )
            }

            EmployeeOrderListItemResponse(
                orderId = o.id!!,
                status = o.status.name,
                totalPrice = o.totalPrice,
                createdAt = o.createdAt,
                pickupTime = o.pickupTime,
                customer = OrderCustomerResponse(
                    id = o.user.id!!,
                    fullName = o.user.fullName,
                    email = o.user.email,
                    imageURL = o.user.imageURL
                ),
                dishes = dishResponses
            )
        }
        return com.ChickenKitchen.app.model.dto.response.EmployeeOrderListPageResponse(
            items = items,
            total = page.totalElements,
            pageNumber = safePage + 1,
            pageSize = safeSize,
            totalPages = page.totalPages,
        )
    }

    @Transactional(readOnly = true)
    override fun getOrderDetailWithIngredients(orderId: Long): EmployeeOrderDetailResponse {
        val user = currentUser()
        val detail = employeeDetailRepository.findByUser(user)
            ?: throw UserNotFoundException("Employee detail not found for user ${user.email}")

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException("Order with id $orderId not found") }

        if (order.store.id != detail.store.id) {
            throw StoreNotFoundException("Order does not belong to employee's store")
        }

        val dishes = dishRepository.findAllByOrderId(order.id!!)
        val qtyMap = orderDishRepository.findAllByOrderId(order.id!!).associateBy({ it.dish.id!! }, { it.quantity })
        val dishResponses = dishes.map { d ->
            val steps = orderStepRepository.findAllByDishId(d.id!!)
            val stepResponses = steps.map { st ->
                val itemResponses = st.items.map { link ->
                    val mi = link.menuItem
                    val ingredients = mi.recipes.map { it.ingredient.toIngredientResponse() }
                    StepItemWithIngredientsResponse(
                        menuItemId = mi.id!!,
                        menuItemName = mi.name,
                        imageUrl = mi.imageUrl,
                        quantity = link.quantity,
                        price = mi.price,
                        cal = mi.cal,
                        ingredients = ingredients
                    )
                }
                StepWithIngredientsResponse(
                    stepId = st.step.id!!,
                    stepName = st.step.name,
                    items = itemResponses
                )
            }
            DishWithIngredientsResponse(
                dishId = d.id!!,
                name = d.name,
                quantity = qtyMap[d.id!!] ?: 1,
                isCustom = d.isCustom,
                note = d.note,
                price = d.price,
                cal = d.cal,
                updatedAt = d.updatedAt,
                steps = stepResponses
            )
        }

        return EmployeeOrderDetailResponse(
            orderId = order.id!!,
            status = order.status.name,
            totalPrice = order.totalPrice,
            createdAt = order.createdAt,
            pickupTime = order.pickupTime,
            customer = OrderCustomerResponse(
                id = order.user.id!!,
                fullName = order.user.fullName,
                email = order.user.email,
                imageURL = order.user.imageURL
            ),
            dishes = dishResponses
        )
    }
}
