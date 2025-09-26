package com.ChickenKitchen.app.serviceImpl.recipe

import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.handler.QuantityMustBeNonNegativeException
import com.ChickenKitchen.app.handler.RecipeAlreadyExistsException
import com.ChickenKitchen.app.handler.RecipeNotFoundException
import com.ChickenKitchen.app.mapper.toRecipeDetailResponse
import com.ChickenKitchen.app.mapper.toRecipeResponse
import com.ChickenKitchen.app.mapper.toRecipeResponseList
import com.ChickenKitchen.app.model.dto.request.CreateRecipeRequest
import com.ChickenKitchen.app.model.dto.request.RecipeIngredientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateRecipeRequest
import com.ChickenKitchen.app.model.dto.response.RecipeDetailResponse
import com.ChickenKitchen.app.model.dto.response.RecipeResponse
import com.ChickenKitchen.app.model.entity.recipe.Recipe
import com.ChickenKitchen.app.model.entity.recipe.RecipeIngredient
import com.ChickenKitchen.app.repository.ingredient.IngredientRepository
import com.ChickenKitchen.app.repository.order.OrderRepository
import com.ChickenKitchen.app.repository.recipe.RecipeRepository
import com.ChickenKitchen.app.service.recipe.RecipeService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import org.springframework.security.core.context.SecurityContextHolder
import com.ChickenKitchen.app.enum.OrderStatus
import com.ChickenKitchen.app.model.entity.order.Order

@Service
class RecipeServiceImpl(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
) : RecipeService {

    override fun getAll(): List<RecipeResponse>? {
        val list = recipeRepository.findAll()
        if (list.isEmpty()) return null
        return list.toRecipeResponseList()
    }

    override fun getById(id: Long): RecipeDetailResponse {
        val recipe = recipeRepository.findById(id).orElse(null)
            ?: throw RecipeNotFoundException("Recipe with id $id not found")
        return recipe.toRecipeDetailResponse()
    }

    override fun create(req: CreateRecipeRequest): RecipeDetailResponse {
        if (recipeRepository.findByName(req.name).isPresent) {
            throw RecipeAlreadyExistsException("Recipe with name ${req.name} already exists")
        }

        val recipe = Recipe(
            name = req.name,
            description = req.description,
            isCustomizable = req.isCustomizable,
            price = req.basePrice,
            cal = req.baseCal,
            ingredientSnapshot = req.ingredientSnapshot,
            isActive = req.isActive,
            image = req.image,
            category = req.category,
        )

        applyRecipeIngredientRelations(recipe, req.items)

        val saved = recipeRepository.save(recipe)
        return saved.toRecipeDetailResponse()
    }

    override fun update(id: Long, req: UpdateRecipeRequest): RecipeDetailResponse {
        val recipe = recipeRepository.findById(id).orElse(null)
            ?: throw RecipeNotFoundException("Recipe with id $id not found")

        req.name?.let { recipe.name = it }
        req.description?.let { recipe.description = it }
        req.isCustomizable?.let { recipe.isCustomizable = it }
        req.basePrice?.let { recipe.price = it }
        req.baseCal?.let { recipe.cal = it }
        req.ingredientSnapshot?.let { recipe.ingredientSnapshot = it }
        req.isActive?.let { recipe.isActive = it }
        req.image?.let { recipe.image = it }
        req.category?.let { recipe.category = it }

        req.items?.let { items ->
            applyRecipeIngredientRelations(recipe, items)
        }

        val saved = recipeRepository.save(recipe)
        return saved.toRecipeDetailResponse()
    }

    override fun delete(id: Long) {
        val recipe = recipeRepository.findById(id).orElse(null)
            ?: throw RecipeNotFoundException("Recipe with id $id not found")
        recipeRepository.delete(recipe)
    }

    override fun changeStatus(id: Long): RecipeResponse {
        val recipe = recipeRepository.findById(id).orElse(null)
            ?: throw RecipeNotFoundException("Recipe with id $id not found")
        recipe.isActive = !recipe.isActive
        val updated = recipeRepository.save(recipe)
        return updated.toRecipeResponse()
    }

    private fun applyRecipeIngredientRelations(
        recipe: Recipe,
        items: List<RecipeIngredientRequest>?
    ) {
        if (items.isNullOrEmpty()) {
            recipe.recipe_ingredients.clear()
            return
        }

        checkForDuplicateIngredients(items)

        val relations = items.map { reqItem ->
            val ingredient = ingredientRepository.findById(reqItem.ingredientId).orElse(null)
                ?: throw IngredientNotFoundException("Ingredient with id ${reqItem.ingredientId} not found")

            if (reqItem.quantity < 0) {
                throw QuantityMustBeNonNegativeException("Quantity must be greater than or equal to 0")
            }

            RecipeIngredient(
                recipe = recipe,
                ingredient = ingredient,
                quantity = reqItem.quantity,
                baseUnit = reqItem.baseUnit,
                price = calculatePrice(ingredient.price, ingredient.baseQuantity, reqItem.quantity),
                cal = calculateCal(ingredient.cal, ingredient.baseQuantity, reqItem.quantity),
                itemSnapshot = null
            )
        }

        recipe.recipe_ingredients.apply {
            clear()
            addAll(relations)
        }

        recalcRecipeValues(recipe)
    }

    private fun calculatePrice(basePrice: BigDecimal, baseQuantity: Int, quantity: Int): BigDecimal {
        if (baseQuantity == 0) return BigDecimal.ZERO
        return basePrice.multiply(BigDecimal(quantity))
            .divide(BigDecimal(baseQuantity), 2, RoundingMode.HALF_UP)
    }

    private fun calculateCal(baseCal: Int, baseQuantity: Int, quantity: Int): Int {
        if (baseQuantity == 0) return 0
        val cal = BigDecimal(baseCal)
            .multiply(BigDecimal(quantity))
            .divide(BigDecimal(baseQuantity), 0, RoundingMode.HALF_UP)
        return cal.toInt()
    }

    private fun recalcRecipeValues(recipe: Recipe) {
        val totalPrice = recipe.recipe_ingredients.fold(BigDecimal.ZERO) { acc, ri -> acc.add(ri.price) }
        val totalCal = recipe.recipe_ingredients.sumOf { it.cal }
        recipe.price = totalPrice
        recipe.cal = totalCal
    }

    private fun checkForDuplicateIngredients(items: List<RecipeIngredientRequest>) {
        val duplicateIds = items.groupBy { it.ingredientId }
            .filter { it.value.size > 1 }
            .keys
        if (duplicateIds.isNotEmpty()) {
            throw IllegalArgumentException("Duplicate ingredientIds found: $duplicateIds")
        }
    }
}

