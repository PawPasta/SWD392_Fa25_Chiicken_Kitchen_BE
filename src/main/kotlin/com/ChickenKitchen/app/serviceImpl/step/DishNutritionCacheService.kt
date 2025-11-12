package com.ChickenKitchen.app.serviceImpl.step

import com.ChickenKitchen.app.model.dto.response.MenuItemNutrientBriefResponse
import com.ChickenKitchen.app.model.entity.order.OrderStep
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.repository.order.OrderStepRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.repository.menu.MenuItemNutrientRepository
import com.ChickenKitchen.app.util.NutrientJsonUtil
import org.springframework.stereotype.Service
import java.math.BigDecimal
import com.ChickenKitchen.app.enums.UnitType

@Service
class DishNutritionCacheService(
    private val orderStepRepository: OrderStepRepository,
    private val menuItemNutrientRepository: MenuItemNutrientRepository,
    private val dishRepository: DishRepository,
) {

    fun ensureSnapshot(dish: Dish, steps: List<OrderStep>? = null): List<MenuItemNutrientBriefResponse> {
        val cached = NutrientJsonUtil.parse(dish.nutrientJson)
        if (cached.isNotEmpty()) return cached
        return refreshSnapshot(dish, steps)
    }

    fun refreshSnapshot(dish: Dish, steps: List<OrderStep>? = null): List<MenuItemNutrientBriefResponse> {
        val dishId = dish.id ?: return emptyList()
        val loadedSteps = steps ?: orderStepRepository.findAllWithItemsByDishId(dishId)
        val aggregated = aggregateFromSteps(loadedSteps)
        dish.nutrientJson = NutrientJsonUtil.toJson(aggregated)
        dishRepository.save(dish)
        return aggregated
    }

    private fun aggregateFromSteps(steps: List<OrderStep>): List<MenuItemNutrientBriefResponse> {
        val items = steps.flatMap { it.items }
        if (items.isEmpty()) return emptyList()

        val totals = mutableMapOf<Long, BigDecimal>()
        val meta = mutableMapOf<Long, Pair<String, UnitType>>()

        items.groupBy { it.menuItem.id!! }
            .forEach { (menuItemId, links) ->
                val multiplier = BigDecimal(links.sumOf { it.quantity })
                menuItemNutrientRepository.findByMenuItemId(menuItemId).forEach { link ->
                    val nutrientId = link.nutrient.id!!
                    totals[nutrientId] = (totals[nutrientId] ?: BigDecimal.ZERO).add(link.quantity.multiply(multiplier))
                    meta.putIfAbsent(nutrientId, link.nutrient.name to link.nutrient.baseUnit)
                }
            }

        return totals.entries.map { (nutrientId, quantity) ->
            val (name, unit) = meta[nutrientId]!!
            MenuItemNutrientBriefResponse(
                id = nutrientId,
                name = name,
                quantity = quantity,
                baseUnit = unit
            )
        }.sortedBy { it.name }
    }
}
