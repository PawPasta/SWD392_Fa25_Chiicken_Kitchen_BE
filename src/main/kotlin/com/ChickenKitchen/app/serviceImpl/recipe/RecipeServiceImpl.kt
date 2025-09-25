// package com.ChickenKitchen.app.serviceImpl.recipe

// import com.ChickenKitchen.app.service.recipe.RecipeService
// import org.springframework.stereotype.Service
// import com.ChickenKitchen.app.model.dto.request.CreateRecipeRequest
// import com.ChickenKitchen.app.model.dto.request.UpdateRecipeRequest
// import com.ChickenKitchen.app.model.dto.request.RecipeIngredientRequest
// import com.ChickenKitchen.app.model.dto.response.RecipeResponse
// import com.ChickenKitchen.app.model.dto.response.RecipeDetailResponse
// import com.ChickenKitchen.app.model.entity.recipe.Recipe
// import com.ChickenKitchen.app.model.entity.recipe.RecipeIngredient
// import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
// import com.ChickenKitchen.app.repository.recipe.RecipeRepository
// import com.ChickenKitchen.app.repository.ingredient.IngredientRepository
// import com.ChickenKitchen.app.repository.cooking.CookingMethodRepository
// import com.ChickenKitchen.app.repository.cooking.CookingEffectRepository
// import com.ChickenKitchen.app.repository.ingredient.IngredientNutrientRepository
// import com.ChickenKitchen.app.model.entity.cooking.CookingMethod
// import com.ChickenKitchen.app.model.entity.ingredient.IngredientNutrient
// import com.ChickenKitchen.app.model.entity.cooking.CookingEffect
// import com.ChickenKitchen.app.enum.EffectType
// import com.ChickenKitchen.app.mapper.toRecipeResponseList
// import com.ChickenKitchen.app.mapper.toRecipeDetailResponse
// import com.ChickenKitchen.app.handler.RecipeNotFoundException
// import com.ChickenKitchen.app.handler.RecipeAlreadyExistsException
// import com.ChickenKitchen.app.handler.IngredientNotFoundException
// import com.ChickenKitchen.app.handler.CookingMethodNotFoundException
// import com.ChickenKitchen.app.handler.QuantityMustBeNonNegativeException
// import java.math.BigDecimal
// import java.math.RoundingMode

// @Service
// class RecipeServiceImpl(
//     private val recipeRepository: RecipeRepository,
//     private val ingredientRepository: IngredientRepository,
//     private val cookingMethodRepository: CookingMethodRepository,
//     private val cookingEffectRepository: CookingEffectRepository,
//     private val ingredientNutrientRepository: IngredientNutrientRepository
// ) : RecipeService {

//     override fun getAll(): List<RecipeResponse>? {
//         val list = recipeRepository.findAll()
//         if (list.isEmpty()) return null
//         return list.toRecipeResponseList()
//     }

//     override fun getById(id: Long): RecipeDetailResponse {
//         val recipe = recipeRepository.findById(id).orElse(null)
//             ?: throw RecipeNotFoundException("Recipe with id $id not found")
//         return recipe.toRecipeDetailResponse()
//     }

//     override fun create(req: CreateRecipeRequest): RecipeDetailResponse {
//         val existing = recipeRepository.findByName(req.name)
//         if (existing.isPresent) {
//             throw RecipeAlreadyExistsException("Recipe with name ${req.name} already exists")
//         }

//         val recipe = Recipe(
//             name = req.name,
//             description = req.description,
//             isCustomizable = req.isCustomizable,
//             basePrice = req.basePrice,
//             baseCal = req.baseCal,
//             ingredientSnapshot = req.ingredientSnapshot,
//             isActive = req.isActive,
//             image = req.image,
//             category = req.category,
//         )

//         // Apply relations to recipe_ingredients if provided
//         applyRecipeIngredientRelations(recipe, req.items)

//         val saved = recipeRepository.save(recipe)
//         return saved.toRecipeDetailResponse()
//     }

//     override fun update(id: Long, req: UpdateRecipeRequest): RecipeDetailResponse {
//         val recipe = recipeRepository.findById(id).orElse(null)
//             ?: throw RecipeNotFoundException("Recipe with id $id not found")

//         req.name?.let { recipe.name = it }
//         req.description?.let { recipe.description = it }
//         req.isCustomizable?.let { recipe.isCustomizable = it }
//         req.basePrice?.let { recipe.basePrice = it }
//         req.baseCal?.let { recipe.baseCal = it }
//         req.ingredientSnapshot?.let { recipe.ingredientSnapshot = it }
//         req.isActive?.let { recipe.isActive = it }
//         req.image?.let { recipe.image = it }
//         req.category?.let { recipe.category = it }

//         // Update relations if provided
//         req.items?.let { items ->
//             applyRecipeIngredientRelations(recipe, items)
//         }

//         val saved = recipeRepository.save(recipe)
//         return saved.toRecipeDetailResponse()
//     }

//     override fun delete(id: Long) {
//         val recipe = recipeRepository.findById(id).orElse(null)
//             ?: throw RecipeNotFoundException("Recipe with id $id not found")
//         recipeRepository.delete(recipe)
//     }
//     private fun checkForDuplicateRecipeItems(items: List<RecipeIngredientRequest>) {
//         val duplicatePairs = items.groupBy { Pair(it.ingredientId, it.cookingMethodId) }
//             .filter { it.value.size > 1 }
//             .keys
//         if (duplicatePairs.isNotEmpty()) {
//             throw IllegalArgumentException("Duplicate ingredientId-cookingMethodId pairs found: $duplicatePairs")
//         }
//     }

//     private fun applyRecipeIngredientRelations(
//         recipe: Recipe,
//         items: List<RecipeIngredientRequest>?
//     ) {
//         if (items.isNullOrEmpty()) {
//             recipe.recipe_ingredients.clear()
//             // No items: leave existing base values unchanged
//             return
//         }

//         checkForDuplicateRecipeItems(items)

//         val relations = items.map { reqItem ->
//             val ingredient = ingredientRepository.findById(reqItem.ingredientId).orElse(null)
//                 ?: throw IngredientNotFoundException("Ingredient with id ${reqItem.ingredientId} not found")
//             val method = cookingMethodRepository.findById(reqItem.cookingMethodId).orElse(null)
//                 ?: throw CookingMethodNotFoundException("Cooking method with id ${reqItem.cookingMethodId} not found")

//             if (reqItem.quantity < 0) {
//                 throw QuantityMustBeNonNegativeException("Quantity must be greater than or equal to 0")
//             }

//             RecipeIngredient(
//                 recipe = recipe,
//                 ingredient = ingredient,
//                 cookingMethod = method,
//                 quantity = reqItem.quantity,
//                 baseUnit = reqItem.baseUnit,
//                 price = calculateItemPrice(ingredient, method, reqItem.quantity),
//                 cal = calculateItemCal(ingredient, method, reqItem.quantity),
//                 itemSnapshot = null
//             )
//         }

//         recipe.recipe_ingredients.apply {
//             clear()
//             addAll(relations)
//         }

//         // After applying relations, recalculate base price and cal
//         recalcRecipeBaseValues(recipe)
//     }

//     private fun calculatePrice(ingredient: Ingredient, quantity: Int): BigDecimal {
//         return ingredient.basePrice.multiply(BigDecimal(quantity))
//             .divide(BigDecimal(ingredient.baseQuantity), 2, RoundingMode.HALF_UP)
//     }

//     private fun calculateItemPrice(ingredient: Ingredient, method: CookingMethod, quantity: Int): BigDecimal {
//         val ingredientPrice = calculatePrice(ingredient, quantity)
//         return ingredientPrice.add(method.basePrice)
//     }

//     private fun calculateCal(ingredient: Ingredient, quantity: Int): Int {
//         if (ingredient.baseQuantity == 0) return 0
//         val cal = ingredient.baseCal.toBigDecimal()
//             .multiply(BigDecimal(quantity))
//             .divide(BigDecimal(ingredient.baseQuantity), 0, RoundingMode.HALF_UP)
//         return cal.toInt()
//     }

//     private fun calculateItemCal(ingredient: Ingredient, method: CookingMethod, quantity: Int): Int {
//         val base = calculateCal(ingredient, quantity)
//         val methodId = method.id ?: return base
//         val ingredientId = ingredient.id ?: return base

//         val effects = cookingEffectRepository.findAllByMethodId(methodId)
//         if (effects.isEmpty()) return base

//         val ingredientNutrients = ingredientNutrientRepository.findAllByIngredientId(ingredientId)
//         if (ingredientNutrients.isEmpty()) return base

//         // Helper to get scaled nutrient amount for the given quantity
//         fun scaledAmount(name: String): BigDecimal {
//             val entry = ingredientNutrients.firstOrNull { it.nutrient.name.equals(name, ignoreCase = true) }
//                 ?: return BigDecimal.ZERO
//             if (ingredient.baseQuantity == 0) return BigDecimal.ZERO
//             return entry.amount.multiply(BigDecimal(quantity))
//                 .divide(BigDecimal(ingredient.baseQuantity), 6, RoundingMode.HALF_UP)
//         }

//         // Aggregate percent effects by nutrient name (increase positive, decrease negative)
//         val pctByNutrient: Map<String, BigDecimal> = effects.groupBy { it.nutrient.name }
//             .mapValues { (_, list) ->
//                 list.fold(BigDecimal.ZERO) { acc, e ->
//                     val signed = if (e.effectType == EffectType.INCREASE) e.value else e.value.negate()
//                     acc.add(signed)
//                 }
//             }

//         // Baseline grams for macros for the provided quantity
//         val proteinG = scaledAmount("Protein")
//         val carbG = scaledAmount("Carbohydrate")
//         val fatG = scaledAmount("Fat")

//         fun adjustedDelta(baseG: BigDecimal, name: String): BigDecimal {
//             val pct = pctByNutrient[name]?.divide(BigDecimal(100), 6, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
//             if (pct.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO
//             return baseG.multiply(pct)
//         }

//         val deltaFat = adjustedDelta(fatG, "Fat")
//         val deltaCarb = adjustedDelta(carbG, "Carbohydrate")
//         val deltaProtein = adjustedDelta(proteinG, "Protein")

//         // Energy densities: Fat 9 kcal/g, Carbs 4 kcal/g, Protein 4 kcal/g
//         val deltaCal = deltaFat.multiply(BigDecimal(9))
//             .add(deltaCarb.multiply(BigDecimal(4)))
//             .add(deltaProtein.multiply(BigDecimal(4)))

//         val finalCal = BigDecimal(base).add(deltaCal).setScale(0, RoundingMode.HALF_UP)
//         return finalCal.toInt()
//     }

//     private fun recalcRecipeBaseValues(recipe: Recipe) {
//         val totalPrice = recipe.recipe_ingredients.fold(BigDecimal.ZERO) { acc, ri -> acc.add(ri.price) }
//         val totalCal = recipe.recipe_ingredients.sumOf { it.cal }
//         recipe.basePrice = totalPrice
//         recipe.baseCal = totalCal
//     }
// }
