package com.ChickenKitchen.app.serviceImpl.payment

import com.ChickenKitchen.app.enums.PaymentStatus
import com.ChickenKitchen.app.handler.OrderNotFoundException
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.repository.payment.PaymentRepository
import com.ChickenKitchen.app.service.payment.PaymentService
import com.ChickenKitchen.app.service.user.WalletService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service


@Service
class PaymentServiceImpl (
    private val paymentRepository: PaymentRepository,
    private val walletService: WalletService
) : PaymentService{

    @Transactional
    override fun refundPayment(order: Order, reason: String?) {
        val payment = paymentRepository.findByOrderId(order.id!!)
            ?: throw OrderNotFoundException("Payment for order ${order.id} not found")

        val refundAmount = payment.finalAmount

        payment.status = PaymentStatus.REFUNDED
        payment.note = reason
        paymentRepository.save(payment)

        walletService.refundToWallet(order.user, payment, refundAmount)
    }

    @Transactional
    override fun rejectRefund(order: Order, reason: String?) {
        val payment = paymentRepository.findByOrderId(order.id!!)
            ?: throw OrderNotFoundException("Payment for order ${order.id} not found")

        payment.status = PaymentStatus.REFUNDED_REJECT
        payment.note = reason
        paymentRepository.save(payment)
    }
}