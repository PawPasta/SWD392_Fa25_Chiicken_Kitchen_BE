package com.ChickenKitchen.app.serviceImpl.combo

import com.ChickenKitchen.app.handler.IngredientAlreadyExistsException
import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.handler.QuantityMustBeNonNegativeException
import com.ChickenKitchen.app.handler.RecipeNotFoundException
import com.ChickenKitchen.app.mapper.toComboDetailResponse
import com.ChickenKitchen.app.mapper.toComboResponse
import com.ChickenKitchen.app.mapper.toComboResponseList
import com.ChickenKitchen.app.model.dto.request.ComboItemRequest
import com.ChickenKitchen.app.model.dto.request.CreateComboRequest
import com.ChickenKitchen.app.model.dto.request.UpdateComboRequest
import com.ChickenKitchen.app.model.dto.response.ComboDetailResponse
import com.ChickenKitchen.app.model.dto.response.ComboResponse
import com.ChickenKitchen.app.model.entity.combo.Combo
import com.ChickenKitchen.app.model.entity.combo.ComboItem
import com.ChickenKitchen.app.repository.combo.ComboItemRepository
import com.ChickenKitchen.app.repository.combo.ComboRepository
import com.ChickenKitchen.app.repository.recipe.RecipeRepository
import com.ChickenKitchen.app.service.combo.ComboService
import org.springframework.stereotype.Service

@Service
class ComboServiceImpl(
    private val comboRepository: ComboRepository,
    private val comboItemRepository: ComboItemRepository,
    private val recipeRepository: RecipeRepository
) : ComboService {

    override fun getAll(): List<ComboResponse>? {
        val list = comboRepository.findAll()
        if (list.isEmpty()) return null
        return list.toComboResponseList()
    }

    override fun getById(id: Long): ComboDetailResponse {
        val combo = comboRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Combo with id $id not found")
        return combo.toComboDetailResponse()
    }

    override fun create(req: CreateComboRequest): ComboDetailResponse {
        if (comboRepository.findByName(req.name).isPresent) {
            throw IngredientAlreadyExistsException("Combo with name ${req.name} already exists")
        }
        if (req.cal < 0) throw QuantityMustBeNonNegativeException("Calories must be non-negative")

        val combo = comboRepository.save(
            Combo(
                name = req.name,
                price = req.price,
                cal = req.cal,
                isActive = req.isActive
            )
        )

        applyComboItems(combo, req.items)
        comboRepository.save(combo)

        return combo.toComboDetailResponse()
    }

    override fun update(id: Long, req: UpdateComboRequest): ComboDetailResponse {
        val combo = comboRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Combo with id $id not found")

        req.name?.let {
            if (it != combo.name && comboRepository.findByName(it).isPresent) {
                throw IngredientAlreadyExistsException("Combo with name $it already exists")
            }
            combo.name = it
        }
        req.price?.let { combo.price = it }
        req.cal?.let { 
            if (it < 0) throw QuantityMustBeNonNegativeException("Calories must be non-negative")
            combo.cal = it 
        }
        req.isActive?.let { combo.isActive = it }

        req.items?.let { items ->
            applyComboItems(combo, items)
        }

        val updated = comboRepository.save(combo)
        return updated.toComboDetailResponse()
    }

    override fun delete(id: Long) {
        val combo = comboRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Combo with id $id not found")
        comboRepository.delete(combo)
    }

    override fun changeStatus(id: Long): ComboResponse {
        val combo = comboRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Combo with id $id not found")
        combo.isActive = !combo.isActive
        val updated = comboRepository.save(combo)
        return updated.toComboResponse()
    }

    private fun applyComboItems(combo: Combo, items: List<ComboItemRequest>?) {
        if (items.isNullOrEmpty()) {
            combo.combo_items.clear()
            return
        }

        val newItems = items.map { reqItem ->
            val recipe = recipeRepository.findById(reqItem.recipeId).orElse(null)
                ?: throw RecipeNotFoundException("Recipe with id ${reqItem.recipeId} not found")
            if (reqItem.quantity < 0) throw QuantityMustBeNonNegativeException("Quantity must be non-negative")
            ComboItem(
                combo = combo,
                recipe = recipe,
                quantity = reqItem.quantity
            )
        }

        combo.combo_items.apply {
            clear()
            addAll(newItems)
        }
    }
}

