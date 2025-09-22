package com.ChickenKitchen.app.serviceImpl.ingredient

import com.ChickenKitchen.app.service.ingredient.IngredientService
import com.ChickenKitchen.app.model.dto.request.CreateIngredientRequest
import com.ChickenKitchen.app.model.dto.request.IngredientNutrientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateIngredientRequest
import com.ChickenKitchen.app.model.dto.response.IngredientResponse
import com.ChickenKitchen.app.model.dto.response.IngredientDetailResponse
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.model.entity.ingredient.IngredientNutrient
import com.ChickenKitchen.app.repository.ingredient.IngredientRepository
import com.ChickenKitchen.app.repository.ingredient.NutrientRepository
import com.ChickenKitchen.app.mapper.toIngredientResponse
import com.ChickenKitchen.app.mapper.toIngredientResponseList
import com.ChickenKitchen.app.mapper.toIngredientDetailResponse
import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.handler.IngredientAlreadyExistsException
import com.ChickenKitchen.app.handler.QuantityMustBeNonNegativeException
import com.ChickenKitchen.app.handler.PriceMustBeNonNegativeException
import com.ChickenKitchen.app.handler.CategoryMustNotBeNullException
import com.ChickenKitchen.app.handler.NutrientNotFoundException
import com.ChickenKitchen.app.handler.DuplicateNutrientInIngredientException
import org.springframework.stereotype.Service

@Service
class IngredientServiceImpl(
    private val ingredientRepository: IngredientRepository,
    private val nutrientRepository: NutrientRepository
) : IngredientService {

    override fun getAll(): List<IngredientResponse>? {
        val list = ingredientRepository.findAll()
        if (list.isEmpty()) return null
        return list.toIngredientResponseList()
    }

    override fun getById(id: Long): IngredientDetailResponse {
        val ingredient = ingredientRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Ingredient with id $id not found")
        return ingredient.toIngredientDetailResponse()
    }

    override fun create(req: CreateIngredientRequest): IngredientDetailResponse {

        if (ingredientRepository.existsByName(req.name)) { 
            throw IngredientAlreadyExistsException("Ingredient with name ${req.name} already exists")
        }

        if (req.basePrice < 0.toBigDecimal()) {
            throw PriceMustBeNonNegativeException("Base price must be greater than or equal to 0")
        }

        if (req.category == null) {
            throw CategoryMustNotBeNullException("Category must not be null")
        }
        
        if (!req.nutrients.isNullOrEmpty()) {
            checkForDuplicateNutrients(req.nutrients)
        }

        val newIngredient = ingredientRepository.save(
            Ingredient(
                name = req.name,
                baseUnit = req.baseUnit,
                baseQuantity = req.baseQuantity,
                basePrice = req.basePrice,
                baseCal = req.baseCal,
                image = req.image,
                category = req.category,
                isActive = req.isActive
            )
        )

        applyNutrientRelations(newIngredient, req.nutrients)
        ingredientRepository.save(newIngredient) // Persist nutrient relations via cascade

        return newIngredient.toIngredientDetailResponse()

    }

    override fun update(id: Long, req: UpdateIngredientRequest): IngredientDetailResponse {
        val ingredient = ingredientRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Ingredient with id $id not found")

        req.name?.let { ingredient.name = it }
        req.baseUnit?.let { ingredient.baseUnit = it }
        req.baseQuantity?.let { ingredient.baseQuantity = it }
        req.basePrice?.let { ingredient.basePrice = it }
        req.baseCal?.let { ingredient.baseCal = it }
        req.image?.let { ingredient.image = it }
        req.category?.let { ingredient.category = it }
        req.isActive?.let { ingredient.isActive = it }

        req.nutrients?.let { nutrientRequests ->
            checkForDuplicateNutrients(req.nutrients)
            applyNutrientRelations(ingredient, nutrientRequests)
        }

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

    private fun applyNutrientRelations(
        ingredient: Ingredient,
        nutrientRequests: List<IngredientNutrientRequest>?
    ) {
        if (nutrientRequests.isNullOrEmpty()) {
            ingredient.ingredient_nutrients.clear()
            return
        }

        val nutrientRelations = nutrientRequests.map { request ->
            val nutrient = nutrientRepository.findById(request.nutrientId).orElse(null)
                ?: throw NutrientNotFoundException("Nutrient with id ${request.nutrientId} not found")

            IngredientNutrient(
                ingredient = ingredient,
                nutrient = nutrient,
                amount = request.amount
            )
        }

        ingredient.ingredient_nutrients.apply {
            clear()
            addAll(nutrientRelations)
        }
    }

    private fun checkForDuplicateNutrients(nutrientRequests: List<IngredientNutrientRequest>) {
        val duplicateIds = nutrientRequests.groupBy { it.nutrientId }
            .filter { it.value.size > 1 }
            .keys

        if (duplicateIds.isNotEmpty()) {
            throw DuplicateNutrientInIngredientException("Duplicate nutrientIds found: $duplicateIds")
        }
    }

}
