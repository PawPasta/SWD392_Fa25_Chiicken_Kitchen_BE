package com.ChickenKitchen.app.serviceImpl.cooking

import com.ChickenKitchen.app.service.cooking.CookingMethodService
import com.ChickenKitchen.app.model.dto.request.CreateCookingMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCookingMethodRequest
import com.ChickenKitchen.app.model.dto.response.CookingMethodResponse
import com.ChickenKitchen.app.model.dto.response.CookingMethodDetailResponse
import com.ChickenKitchen.app.model.entity.cooking.CookingMethod
import com.ChickenKitchen.app.repository.cooking.CookingMethodRepository
import com.ChickenKitchen.app.mapper.toCookingMethodResponse
import com.ChickenKitchen.app.mapper.toCookingMethodResponseList
import com.ChickenKitchen.app.mapper.toCookingMethodDetailResponse
import com.ChickenKitchen.app.handler.CookingMethodNotFoundException
import org.springframework.stereotype.Service

@Service
class CookingMethodServiceImpl(
    private val cookingMethodRepository: CookingMethodRepository
) : CookingMethodService {

    override fun getAll(): List<CookingMethodResponse>? {
        val list = cookingMethodRepository.findAll()
        if (list.isEmpty()) return null
        return list.toCookingMethodResponseList()
    }

    override fun getById(id: Long): CookingMethodDetailResponse? {
        val method = cookingMethodRepository.findById(id).orElse(null)
            ?: throw CookingMethodNotFoundException("CookingMethod with id $id not found")
        return method.toCookingMethodDetailResponse()
    }

    override fun create(req: CreateCookingMethodRequest): CookingMethodDetailResponse {
        val newMethod = cookingMethodRepository.save(
            CookingMethod(
                name = req.name,
                note = req.note,
                basePrice = req.basePrice
            )
        )
        return newMethod.toCookingMethodDetailResponse()
    }

    override fun update(id: Long, req: UpdateCookingMethodRequest): CookingMethodDetailResponse {
        val method = cookingMethodRepository.findById(id).orElse(null)
            ?: throw CookingMethodNotFoundException("CookingMethod with id $id not found")

        req.name?.let { method.name = it }
        req.note?.let { method.note = it }
        req.basePrice?.let { method.basePrice = it }

        val updated = cookingMethodRepository.save(method)
        return updated.toCookingMethodDetailResponse()
    }

    override fun delete(id: Long) {
        val method = cookingMethodRepository.findById(id).orElse(null)
            ?: throw CookingMethodNotFoundException("CookingMethod with id $id not found")
        cookingMethodRepository.delete(method)
    }
}
