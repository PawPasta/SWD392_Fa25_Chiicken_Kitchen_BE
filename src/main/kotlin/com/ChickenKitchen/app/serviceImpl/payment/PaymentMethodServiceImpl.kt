package com.ChickenKitchen.app.serviceImpl.payment

import com.ChickenKitchen.app.handler.PaymentMethodHasTransactionsException
import com.ChickenKitchen.app.handler.PaymentMethodNameExistException
import com.ChickenKitchen.app.handler.PaymentMethodNotFoundException
import com.ChickenKitchen.app.mapper.toListPaymentMethodResponse
import com.ChickenKitchen.app.mapper.toPaymentMethodResponse
import com.ChickenKitchen.app.model.dto.request.CreatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.response.PaymentMethodResponse
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.service.payment.PaymentMethodService
import org.springframework.stereotype.Service
import org.springframework.data.domain.PageRequest
import kotlin.math.max


@Service
class PaymentMethodServiceImpl (
    private val paymentMethodRepository: PaymentMethodRepository
): PaymentMethodService {
    override fun getAll(): List<PaymentMethodResponse>? {
        val list = paymentMethodRepository.findAll()
        if(list.isEmpty()) return null
        return list.toListPaymentMethodResponse()
    }

    override fun getAll(pageNumber: Int, size: Int): List<PaymentMethodResponse>? {
        val safeSize = max(size, 1)
        val safePage = max(pageNumber, 1)
        val pageable = PageRequest.of(safePage - 1, safeSize)
        val page = paymentMethodRepository.findAll(pageable)
        if (page.isEmpty) return null
        return page.content.toListPaymentMethodResponse()
    }

    override fun getById(id: Long): PaymentMethodResponse {
        val method = paymentMethodRepository.findById(id)
            .orElseThrow { PaymentMethodNotFoundException("Cannot find PaymentMethod with id $id") }
        return method.toPaymentMethodResponse()
    }

    override fun create(req: CreatePaymentMethodRequest): PaymentMethodResponse {
        val existedByName = paymentMethodRepository.findByName(req.name)
        if(existedByName != null){
            throw PaymentMethodNameExistException("Payment Method Name is already exists")
        }
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
        val method = paymentMethodRepository.findById(id)
            .orElseThrow { PaymentMethodNotFoundException("Cannot find PaymentMethod with id $id") }

        method.name = req.name ?: method.name
        method.description = req.description ?: method.description
        method.isActive = req.isActive

        val saved = paymentMethodRepository.save(method)
        return saved.toPaymentMethodResponse()
    }

    // Delete tam thoi khong ghi
    override fun delete(id: Long) {
        val method = paymentMethodRepository.findById(id)
            .orElseThrow { PaymentMethodNotFoundException("Cannot find PaymentMethod with id $id") }

        if (method.transactions.isNotEmpty()) {
            throw PaymentMethodHasTransactionsException(
                "Cannot delete PaymentMethod with id $id: it has ${method.transactions.size} transactions"
            )
        }

        paymentMethodRepository.delete(method)
    }

    override fun changeStatus(id: Long): PaymentMethodResponse {
        val method = paymentMethodRepository.findById(id)
            .orElseThrow { PaymentMethodNotFoundException("Cannot find PaymentMethod with id $id") }

        method.isActive = !method.isActive

        val save = paymentMethodRepository.save(method)

        return save.toPaymentMethodResponse()
    }
    override fun count(): Long = paymentMethodRepository.count()
}
