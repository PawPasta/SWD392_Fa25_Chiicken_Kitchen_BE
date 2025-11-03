package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.handler.InvalidOrderStatusException
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.handler.StoreNotFoundException
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.model.dto.response.CurrentDishResponse
import com.ChickenKitchen.app.model.dto.response.CurrentStepItemResponse
import com.ChickenKitchen.app.model.dto.response.CurrentStepResponse
import com.ChickenKitchen.app.model.dto.response.OrderBriefResponse
import com.ChickenKitchen.app.model.dto.response.OrderCurrentResponse
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.order.OrderStepRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.repository.user.EmployeeDetailRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.order.EmployeeOrderService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class EmployeeOrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderStepRepository: OrderStepRepository,
    private val dishRepository: DishRepository,
    private val userRepository: UserRepository,
    private val employeeDetailRepository: EmployeeDetailRepository,
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

    override fun getConfirmedOrdersForEmployeeStore(): List<OrderBriefResponse> {
        val user = currentUser()
        val detail = employeeDetailRepository.findByUser(user)
            ?: throw UserNotFoundException("Employee detail not found for user ${user.email}")

        val storeId = detail.store.id
            ?: throw StoreNotFoundException("Store not found for employee ${user.email}")

        val orders = orderRepository.findAllByStoreIdAndStatusOrderByCreatedAtDesc(
            storeId,
            OrderStatus.CONFIRMED
        )

        return orders.map { o ->
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
}
