package com.ChickenKitchen.app.serviceImpl.order

import com.ChickenKitchen.app.enums.DiscountType
import com.ChickenKitchen.app.enums.OrderStatus
import com.ChickenKitchen.app.enums.PaymentStatus
import com.ChickenKitchen.app.handler.InvalidOrderStatusException
import com.ChickenKitchen.app.handler.InvalidOrderStepException
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.handler.PaymentMethodNameNotAvailable
import com.ChickenKitchen.app.handler.PaymentMethodNotFoundException
import com.ChickenKitchen.app.handler.PromotionNotFoundException
import com.ChickenKitchen.app.handler.PromotionNotValidThisTime
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.model.dto.request.OrderConfirmRequest
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.payment.Payment
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.model.entity.promotion.OrderPromotion
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.payment.PaymentRepository
import com.ChickenKitchen.app.repository.promotion.OrderPromotionRepository
import com.ChickenKitchen.app.repository.promotion.PromotionRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.service.order.OrderPaymentService
import com.ChickenKitchen.app.service.payment.TransactionService
import com.ChickenKitchen.app.service.payment.VNPayService
import com.ChickenKitchen.app.service.user.WalletService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrderPaymentServiceImpl(
    private val orderRepository: OrderRepository,
    private val promotionRepository: PromotionRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val orderPromotionRepository: OrderPromotionRepository,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    private val dishRepository: DishRepository,

    private val vnPayService: VNPayService,
    private val walletService: WalletService,
    private val transactionService: TransactionService

) : OrderPaymentService {

    private fun currentUser() : User {
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

    @Transactional
    override fun confirmOrder(req: OrderConfirmRequest): String {
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

        val user = currentUser()

        // Ensure order total is up-to-date before confirming
        val sum = dishRepository.findAllByOrderId(order.id!!).sumOf { it.price }
        if (order.totalPrice != sum) {
            order.totalPrice = sum
            orderRepository.save(order)
        }

        val existingPayment = paymentRepository.findByOrderId(order.id!!)

        var discountAmount: Int
        var finalAmount: Int

        if (existingPayment != null && existingPayment.status == PaymentStatus.PENDING) {
            // Reconfirm: keep existing discount, update amounts to reflect current order total
            discountAmount = existingPayment.discountAmount
            finalAmount = (order.totalPrice - discountAmount).coerceAtLeast(0)
            existingPayment.amount = order.totalPrice
            existingPayment.finalAmount = finalAmount
            paymentRepository.save(existingPayment)
        } else {
            // Fresh confirm or retry after FAILED: apply optional promotion
            var computedDiscount = 0
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

                computedDiscount = if (promo.discountType == DiscountType.PERCENT) {
                    (order.totalPrice * promo.discountValue) / 100
                } else {
                    promo.discountValue
                }

                // Save OrderPromotion and decrement quantity
                orderPromotionRepository.save(
                    OrderPromotion(order = order, promotion = promo, user = user, usedDate = now)
                )
                promo.quantity -= 1
                promotionRepository.save(promo)
            }

            discountAmount = computedDiscount
            finalAmount = (order.totalPrice - discountAmount).coerceAtLeast(0)

            if (existingPayment != null && order.status == OrderStatus.FAILED) {
                // Retry after FAILED
                existingPayment.amount = order.totalPrice
                existingPayment.discountAmount = discountAmount
                existingPayment.finalAmount = finalAmount
                existingPayment.status = PaymentStatus.PENDING
                paymentRepository.save(existingPayment)
            } else if (existingPayment == null) {
                // Create new payment if none
                Payment(
                    user = user,
                    order = order,
                    amount = order.totalPrice,
                    discountAmount = discountAmount,
                    finalAmount = finalAmount,
                    status = PaymentStatus.PENDING
                ).also { paymentRepository.save(it) }
            } else {

                throw InvalidOrderStepException("This order already has a payment and cannot be confirmed again.")
            }
        }

        // Guard invalid VNPay amount range (VND, VNPay expects amount*100 in request)
        if (finalAmount !in 5000..<1_000_000_000) {
            throw InvalidOrderStepException("Invalid payment amount: $finalAmount VND. Must be between 5,000 and < 1,000,000,000")
        }

        return processPayment(order, paymentMethod, finalAmount, req.channel)
    }

    override fun processPayment(
        order: Order,
        paymentMethod: PaymentMethod,
        amount: Int,
        channel: String?
    ): String {
        val payment = paymentRepository.findByOrderId(order.id!!)
            ?: throw InvalidOrderStepException("Payment not found for order ${order.id}")

        return when (paymentMethod.name.uppercase()) {
            "VNPAY" -> {
                vnPayService.createVnPayURL(order, channel)
            }
            "WALLET" -> {
                walletService.deductFromWallet(order.user, amount)
                transactionService.createPaymentTransaction(payment,order,paymentMethod)
                "Payment via wallet successful"
            }

            "MOMO" -> {
                throw PaymentMethodNameNotAvailable("MoMo payment method not yet supported")
            }

            else -> throw PaymentMethodNameNotAvailable(
                "Payment method ${paymentMethod.name} is not supported yet"
            )
        }
    }


}
