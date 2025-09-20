package com.ChickenKitchen.app.serviceImpl.ingredient

import com.ChickenKitchen.app.service.ingredient.IngredientService
import com.ChickenKitchen.app.model.dto.request.CreateIngredientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateIngredientRequest
import com.ChickenKitchen.app.model.dto.response.IngredientResponse
import com.ChickenKitchen.app.model.dto.response.IngredientDetailResponse
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.repository.ingredient.IngredientRepository
import com.ChickenKitchen.app.mapper.toIngredientResponse
import com.ChickenKitchen.app.mapper.toIngredientResponseList
import com.ChickenKitchen.app.mapper.toIngredientDetailResponse
import com.ChickenKitchen.app.handler.IngredientNotFoundException
import org.springframework.stereotype.Service

@Service
class IngredientServiceImpl(
    private val ingredientRepository: IngredientRepository
) : IngredientService {

    override fun getAll(): List<IngredientResponse>? {
        val list = ingredientRepository.findAll()
        if (list.isEmpty()) return null
        return list.toIngredientResponseList()
    }

    override fun getById(id: Long): IngredientDetailResponse? {
        val ingredient = ingredientRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Ingredient with id $id not found")
        return ingredient.toIngredientDetailResponse()
    }

    override fun create(req: CreateIngredientRequest): IngredientDetailResponse {
        val newIngredient = ingredientRepository.save(
            Ingredient(
                name = req.name,
                baseUnit = req.baseUnit,
                baseQuantity = req.baseQuantity,
                basePrice = req.basePrice,
                image = req.image,
                category = req.category,
                isActive = req.isActive
            )
        )
        return newIngredient.toIngredientDetailResponse()
    }

    override fun update(id: Long, req: UpdateIngredientRequest): IngredientDetailResponse {
        val ingredient = ingredientRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Ingredient with id $id not found")

        req.name?.let { ingredient.name = it }
        req.baseUnit?.let { ingredient.baseUnit = it }
        req.baseQuantity?.let { ingredient.baseQuantity = it }
        req.basePrice?.let { ingredient.basePrice = it }
        req.image?.let { ingredient.image = it }
        req.category?.let { ingredient.category = it }
        req.isActive?.let { ingredient.isActive = it }

        val updated = ingredientRepository.save(ingredient)
        return updated.toIngredientDetailResponse()
    }

    override fun delete(id: Long) {
        val ingredient = ingredientRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Ingredient with id $id not found")
        ingredientRepository.delete(ingredient)
    }

    override fun changeStatus(id: Long): IngredientResponse {
        val ingredient = ingredientRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Ingredient with id $id not found")
        ingredient.isActive = !ingredient.isActive
        val updated = ingredientRepository.save(ingredient)
        return updated.toIngredientResponse()
    }
}
