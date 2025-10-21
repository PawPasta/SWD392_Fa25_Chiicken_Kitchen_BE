package com.ChickenKitchen.app.serviceImpl.ingredient

import com.ChickenKitchen.app.handler.IngredientHasBatchesException
import com.ChickenKitchen.app.handler.IngredientNameExistException
import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.mapper.toIngredientDetailResponse
import com.ChickenKitchen.app.mapper.toListIngredientResponse
import com.ChickenKitchen.app.model.dto.request.CreateIngredientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateIngredientRequest
import com.ChickenKitchen.app.model.dto.response.IngredientDetailResponse
import com.ChickenKitchen.app.model.dto.response.IngredientResponse
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.model.entity.ingredient.StoreIngredientBatch
import com.ChickenKitchen.app.repository.ingredient.IngredientRepository
import com.ChickenKitchen.app.repository.ingredient.StoreIngredientBatchRepository
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.service.ingredient.IngredientService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class IngredientServiceImpl (
    private val ingredientRepository: IngredientRepository,
    private val ingredientBatchRepository: StoreIngredientBatchRepository,
    private val storeRepository: StoreRepository,
) : IngredientService{

    override fun changeStatus(id: Long): IngredientDetailResponse {
        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { NoSuchElementException("Cannot find Ingredient with id $id") }

        ingredient.isActive = !ingredient.isActive
        ingredientRepository.save(ingredient)
        return ingredient.toIngredientDetailResponse()
    }

    override fun getAll(): List<IngredientResponse>? {
        val list = ingredientRepository.findAll()
        if (list.isEmpty()) return null
        return list.toListIngredientResponse()
    }

    override fun getById(id: Long): IngredientDetailResponse {
        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { IngredientNotFoundException("Ingredient with id $id not found") }
        return ingredient.toIngredientDetailResponse()
    }

    override fun create(req: CreateIngredientRequest): IngredientDetailResponse {

        if (ingredientRepository.findByName(req.name) != null) {
            throw IngredientNameExistException("Ingredient name '${req.name}' already exists")
        }

        val stores = storeRepository.findAllById(req.storeIds)

        if (stores.isEmpty()) {
            throw NoSuchElementException("Cannot find any stores with ids ${req.storeIds}")
        }

        val ingredient = Ingredient(
            name = req.name,
            baseUnit = req.baseUnit,
            batchNumber = req.batchNumber,
            isActive = req.isActive,
            imageUrl = req.imageUrl,
        )

        val savedIngredient = ingredientRepository.save(ingredient)


        val batches = stores.map { store ->
            StoreIngredientBatch(
                store = store,
                ingredient = savedIngredient,
                quantity = req.quantity
            )
        }

        ingredientBatchRepository.saveAll(batches)


        savedIngredient.storeBatches.addAll(batches)

        return savedIngredient.toIngredientDetailResponse()
    }

    @Transactional
    override fun update(id: Long, req: UpdateIngredientRequest): IngredientDetailResponse {
        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { IngredientNotFoundException("Ingredient with id $id not found") }

        req.name?.let { ingredient.name = it }
        req.imageUrl?.let { ingredient.imageUrl = it }
        req.baseUnit.let { ingredient.baseUnit = it }
        req.batchNumber?.let { ingredient.batchNumber = it }
        req.isActive.let { ingredient.isActive = it }


        if (req.quantity != null && ingredient.storeBatches.isNotEmpty()) {
            val firstBatch = ingredient.storeBatches.first()
            firstBatch.quantity = req.quantity
            ingredientBatchRepository.save(firstBatch)
        }

        val updated = ingredientRepository.save(ingredient)
        return updated.toIngredientDetailResponse()
    }


    override fun delete(id: Long) {
        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { IngredientNotFoundException("Ingredient with id $id not found") }

        if (ingredient.storeBatches.isNotEmpty()) {
            throw IngredientHasBatchesException("Cannot delete ingredient with id $id because it has batches")
        }

        ingredientRepository.delete(ingredient)
    }
}