package com.ChickenKitchen.app.serviceImpl.ingredient

import com.ChickenKitchen.app.service.ingredient.NutrientService
import com.ChickenKitchen.app.model.dto.request.CreateNutrientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateNutrientRequest
import com.ChickenKitchen.app.model.dto.response.NutrientResponse
import com.ChickenKitchen.app.model.dto.response.NutrientDetailResponse
import com.ChickenKitchen.app.model.entity.ingredient.Nutrient
import com.ChickenKitchen.app.repository.ingredient.NutrientRepository
import com.ChickenKitchen.app.mapper.toNutrientResponse
import com.ChickenKitchen.app.mapper.toNutrientResponseList
import com.ChickenKitchen.app.mapper.toNutrientDetailResponse
import com.ChickenKitchen.app.handler.NutrientNotFoundException
import org.springframework.stereotype.Service

@Service
class NutrientServiceImpl(
    private val nutrientRepository: NutrientRepository
) : NutrientService {

    override fun getAll(): List<NutrientResponse>? {
        val list = nutrientRepository.findAll()
        if (list.isEmpty()) return null
        return list.toNutrientResponseList()
    }

    override fun getById(id: Long): NutrientDetailResponse {
        val nutrient = nutrientRepository.findById(id).orElse(null)
            ?: throw NutrientNotFoundException("Nutrient with id $id not found")
        return nutrient.toNutrientDetailResponse()
    }

    override fun create(req: CreateNutrientRequest): NutrientDetailResponse {

        if (nutrientRepository.existsByName(req.name)) throw NutrientNotFoundException("Nutrient with name ${req.name} already exists")

        val newNutrient = nutrientRepository.save(
            Nutrient(
                name = req.name,
                baseUnit = req.baseUnit
            )
        )
        return newNutrient.toNutrientDetailResponse()
    }

    override fun update(id: Long, req: UpdateNutrientRequest): NutrientDetailResponse {
        val nutrient = nutrientRepository.findById(id).orElse(null)
            ?: throw NutrientNotFoundException("Nutrient with id $id not found")

        if (req.name != null && req.name != nutrient.name) {
            if (nutrientRepository.existsByName(req.name)) {
                throw NutrientNotFoundException("Nutrient with name ${req.name} already exists")
            }
        }

        req.name?.let { nutrient.name = it }
        req.baseUnit?.let { nutrient.baseUnit = it }

        val updated = nutrientRepository.save(nutrient)
        return updated.toNutrientDetailResponse()
    }

    override fun delete(id: Long) {
        val nutrient = nutrientRepository.findById(id).orElse(null)
            ?: throw NutrientNotFoundException("Nutrient with id $id not found")
        nutrientRepository.delete(nutrient)
    }
}
