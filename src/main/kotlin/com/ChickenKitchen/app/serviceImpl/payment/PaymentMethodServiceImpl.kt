package com.ChickenKitchen.app.serviceImpl.payment

import com.ChickenKitchen.app.handler.IngredientAlreadyExistsException
import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.mapper.toPaymentMethodDetailResponse
import com.ChickenKitchen.app.mapper.toPaymentMethodResponse
import com.ChickenKitchen.app.mapper.toPaymentMethodResponseList
import com.ChickenKitchen.app.model.dto.request.CreatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePaymentMethodRequest
import com.ChickenKitchen.app.model.dto.response.PaymentMethodDetailResponse
import com.ChickenKitchen.app.model.dto.response.PaymentMethodResponse
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.service.payment.PaymentMethodService
import org.springframework.stereotype.Service

@Service
class PaymentMethodServiceImpl(
    private val paymentMethodRepository: PaymentMethodRepository
) : PaymentMethodService {

    override fun getAll(): List<PaymentMethodResponse>? {
        val list = paymentMethodRepository.findAll()
        if (list.isEmpty()) return null
        return list.toPaymentMethodResponseList()
    }

    override fun getById(id: Long): PaymentMethodDetailResponse {
        val method = paymentMethodRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Payment method with id $id not found")
        return method.toPaymentMethodDetailResponse()
    }

    override fun create(req: CreatePaymentMethodRequest): PaymentMethodDetailResponse {
        if (paymentMethodRepository.findByName(req.name).isPresent) {
            throw IngredientAlreadyExistsException("Payment method with name ${req.name} already exists")
        }

        val saved = paymentMethodRepository.save(
            PaymentMethod(
                name = req.name,
                description = req.description,
                isActive = req.isActive
            )
        )
        return saved.toPaymentMethodDetailResponse()
    }

    override fun update(id: Long, req: UpdatePaymentMethodRequest): PaymentMethodDetailResponse {
        val method = paymentMethodRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Payment method with id $id not found")

        req.name?.let {
            if (it != method.name && paymentMethodRepository.findByName(it).isPresent) {
                throw IngredientAlreadyExistsException("Payment method with name $it already exists")
            }
            method.name = it
        }
        req.description?.let { method.description = it }
        req.isActive?.let { method.isActive = it }

        val updated = paymentMethodRepository.save(method)
        return updated.toPaymentMethodDetailResponse()
    }

    override fun delete(id: Long) {
        val method = paymentMethodRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Payment method with id $id not found")
        paymentMethodRepository.delete(method)
    }

    override fun changeStatus(id: Long): PaymentMethodResponse {
        val method = paymentMethodRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Payment method with id $id not found")
        method.isActive = !method.isActive
        val updated = paymentMethodRepository.save(method)
        return updated.toPaymentMethodResponse()
    }
}

