package com.ChickenKitchen.app.serviceImpl.transaction

import com.ChickenKitchen.app.mapper.toListPaymentMethodResponse
import com.ChickenKitchen.app.mapper.toPaymentMethodResponse
import com.ChickenKitchen.app.model.dto.request.CreatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.response.PaymentMethodResponse
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.service.transaction.PaymentMethodService
import org.springframework.stereotype.Service


@Service
class PaymentMethodServiceImpl (
    private val paymentMethodRepository: PaymentMethodRepository
): PaymentMethodService {
    override fun getAll(): List<PaymentMethodResponse>? {
        val list = paymentMethodRepository.findAll()
        if(list.isEmpty()) return null
        return list.toListPaymentMethodResponse()
    }

    override fun getById(id: Long): PaymentMethodResponse {
        val paymentMethod = paymentMethodRepository.findById(id).orElseThrow { NoSuchElementException("Cannot fine paymentMethod with $id") }
        return paymentMethod.toPaymentMethodResponse()
    }

    override fun create(req: CreatePaymentMethodRequest): PaymentMethodResponse {
        val paymentMethod = PaymentMethod(
            name = req.name,
            description = req.description,
        )

        val save = paymentMethodRepository.save(paymentMethod)

        return save.toPaymentMethodResponse()
    }

    override fun update(
        id: Long,
        req: UpdatePaymentMethodRequest
    ): PaymentMethodResponse {
        val paymentMethod = paymentMethodRepository.findById(id).orElseThrow { NoSuchElementException("Cannot fine paymentMethod with $id") }

        val updateMethod = PaymentMethod(
            name = req.name ?: paymentMethod.name,
            description = req.description ?: paymentMethod.description,
            isActive = req.isActive
        )
        val saved = paymentMethodRepository.save(updateMethod)

        return saved.toPaymentMethodResponse()
    }

    // Delete tam thoi khong ghi
    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override fun changeStatus(id: Long): PaymentMethodResponse {
        val paymentMethod = paymentMethodRepository.findById(id).orElseThrow { NoSuchElementException("Cannot fine paymentMethod with $id") }
        paymentMethod.isActive = !paymentMethod.isActive

        val save = paymentMethodRepository.save(paymentMethod)

        return save.toPaymentMethodResponse()
    }


}