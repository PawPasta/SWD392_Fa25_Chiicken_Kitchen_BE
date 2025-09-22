package com.ChickenKitchen.app.serviceImpl.cooking

import com.ChickenKitchen.app.service.cooking.CookingEffectService
import com.ChickenKitchen.app.model.dto.request.CreateCookingEffectRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCookingEffectRequest
import com.ChickenKitchen.app.model.dto.response.CookingEffectResponse
import com.ChickenKitchen.app.model.dto.response.CookingEffectDetailResponse
import com.ChickenKitchen.app.model.entity.cooking.CookingEffect
import com.ChickenKitchen.app.repository.cooking.CookingEffectRepository
import com.ChickenKitchen.app.repository.cooking.CookingMethodRepository
import com.ChickenKitchen.app.repository.ingredient.NutrientRepository
import com.ChickenKitchen.app.mapper.toCookingEffectResponse
import com.ChickenKitchen.app.mapper.toCookingEffectResponseList
import com.ChickenKitchen.app.mapper.toCookingEffectDetailResponse
import com.ChickenKitchen.app.handler.CookingEffectNotFoundException
import org.springframework.stereotype.Service

@Service
class CookingEffectServiceImpl(
    private val cookingEffectRepository: CookingEffectRepository,
    private val cookingMethodRepository: CookingMethodRepository,
    private val nutrientRepository: NutrientRepository
) : CookingEffectService {

    override fun getAll(): List<CookingEffectResponse>? {
        val list = cookingEffectRepository.findAll()
        if (list.isEmpty()) return null
        return list.toCookingEffectResponseList()
    }

    override fun getById(id: Long): CookingEffectDetailResponse {
        val effect = cookingEffectRepository.findById(id).orElse(null) 
            ?: throw CookingEffectNotFoundException("CookingEffect with id $id not found")
        return effect.toCookingEffectDetailResponse()
    }

    override fun create(req: CreateCookingEffectRequest): CookingEffectDetailResponse {
        val method = cookingMethodRepository.findById(req.methodId).orElse(null)
            ?: throw CookingEffectNotFoundException("CookingMethod with id ${req.methodId} not found")

        val nutrient = nutrientRepository.findById(req.nutrientId).orElse(null)
            ?: throw CookingEffectNotFoundException("Nutrient with id ${req.nutrientId} not found")

        val newEffect = cookingEffectRepository.save(
            CookingEffect(
                method = method,
                nutrient = nutrient,
                effectType = req.effectType,
                value = java.math.BigDecimal.valueOf(req.value.toLong()),
                description = req.description
            )
        )
        return newEffect.toCookingEffectDetailResponse()
    }

    override fun update(id: Long, req: UpdateCookingEffectRequest): CookingEffectDetailResponse {
        val effect = cookingEffectRepository.findById(id).orElse(null)
            ?: throw CookingEffectNotFoundException("CookingEffect with id $id not found")

        req.methodId?.let {
            val method = cookingMethodRepository.findById(it).orElse(null)
                ?: throw CookingEffectNotFoundException("CookingMethod with id $it not found")
            effect.method = method
        }
        req.nutrientId?.let { id ->
            val nutrient = nutrientRepository.findById(id).orElse(null)
                ?: throw CookingEffectNotFoundException("Nutrient with id $id not found")
            effect.nutrient = nutrient
        }
        req.effectType?.let { effect.effectType = it }
        req.value?.let { effect.value = java.math.BigDecimal.valueOf(it.toLong()) }
        req.description?.let { effect.description = it }

        val updated = cookingEffectRepository.save(effect)
        return updated.toCookingEffectDetailResponse()
    }

    override fun delete(id: Long) {
        val effect = cookingEffectRepository.findById(id).orElse(null)
            ?: throw CookingEffectNotFoundException("CookingEffect with id $id not found")
        cookingEffectRepository.delete(effect)
    }
}
