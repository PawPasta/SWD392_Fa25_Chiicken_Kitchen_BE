package com.ChickenKitchen.app.serviceImpl.step

import com.ChickenKitchen.app.handler.DeleteDishFailedException
import com.ChickenKitchen.app.handler.DishNotFoundException
import com.ChickenKitchen.app.mapper.toDishDetailResponse
import com.ChickenKitchen.app.mapper.toDishResponse
import com.ChickenKitchen.app.model.dto.request.CreateDishBaseRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishBaseRequest
import com.ChickenKitchen.app.model.dto.response.DishDetailResponse
import com.ChickenKitchen.app.model.dto.response.DishResponse
import com.ChickenKitchen.app.model.dto.response.DishSearchResponse
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.repository.order.OrderDishRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.repository.order.OrderStepRepository
import com.ChickenKitchen.app.service.step.DishService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import jakarta.persistence.criteria.JoinType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import kotlin.math.max

@Service
class DishServiceImpl(
    private val dishRepository: DishRepository,
    private val orderDishRepository: OrderDishRepository,
    private val orderStepRepository: OrderStepRepository,
    private val dishNutritionCacheService: DishNutritionCacheService,
) : DishService {

    private fun currentUserEmail(): String {
        val auth = SecurityContextHolder.getContext().authentication
        val email = when (val principal = auth?.principal) {
            is UserDetails -> principal.username
            is String -> if (principal.contains("@")) principal else auth.name
            else -> auth?.name
        }
        if (email.isNullOrBlank() || !email.contains("@")) {
            throw com.ChickenKitchen.app.handler.UserNotFoundException("Authenticated email not found in security context")
        }
        return email
    }

    override fun getAll(): List<DishResponse>? {
        val list = dishRepository.findAllByIsCustomFalse(Sort.by(Sort.Direction.DESC, "createdAt"))
        if (list.isEmpty()) return null
        return mapDishResponses(list)
    }

    override fun getAll(pageNumber: Int, size: Int): List<DishResponse>? {
        val safeSize = max(size, 1)
        val safePage = max(pageNumber, 1)
        val pageable = PageRequest.of(safePage - 1, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"))
        val page = dishRepository.findAllByIsCustomFalse(pageable)
        if (page.isEmpty) return null
        return mapDishResponses(page.content)
    }

    override fun getById(id: Long): DishDetailResponse {
        val dish = dishRepository.findById(id).orElseThrow { DishNotFoundException("Dish with id $id not found") }
        val rawSteps = orderStepRepository.findAllWithItemsByDishId(dish.id!!)
        val nutrients = dishNutritionCacheService.ensureSnapshot(dish, rawSteps)
        val sorted = rawSteps.sortedBy { it.step.stepNumber }
        return dish.toDishDetailResponse(sorted, nutrients)
    }

    override fun create(req: CreateDishBaseRequest): DishDetailResponse {
        val entity = Dish(
            name = req.name,
            price = req.price,
            cal = req.cal,
            isCustom = req.isCustom,
            note = req.note,
            imageUrl = req.imageUrl,
        )
        val saved = dishRepository.save(entity)
        return saved.toDishDetailResponse()
    }

    override fun update(id: Long, req: UpdateDishBaseRequest): DishDetailResponse {
        val current = dishRepository.findById(id).orElseThrow { DishNotFoundException("Dish with id $id not found") }
        val updated = Dish(
            id = current.id,
            name = req.name ?: current.name,
            price = req.price ?: current.price,
            cal = req.cal ?: current.cal,
            isCustom = req.isCustom ?: current.isCustom,
            note = req.note ?: current.note,
            imageUrl = req.imageUrl ?: current.imageUrl,
            createdAt = current.createdAt,
            updatedAt = current.updatedAt,
            orderSteps = current.orderSteps,
            nutrientJson = current.nutrientJson,
        )
        val saved = dishRepository.save(updated)
        return saved.toDishDetailResponse()
    }

    override fun delete(id: Long) {
        val current = dishRepository.findById(id).orElseThrow { DishNotFoundException("Dish with id $id not found") }
        val linkedOrderId = orderDishRepository.findOrderIdByDishId(current.id!!)
        if (linkedOrderId != null) {
            throw DeleteDishFailedException("Dish is linked to order $linkedOrderId; cannot delete")
        }
        dishRepository.delete(current)
    }

    override fun count(): Long = dishRepository.count()

    override fun getMyCustomDishes(): List<DishResponse>? {
        val email = currentUserEmail()
        val list = dishRepository.findAllCustomByUserEmailOrderByCreatedAtDesc(email)
        if (list.isEmpty()) return null
        return mapDishResponses(list)
    }

    override fun searchByComponents(menuItemIds: List<Long>?, keyword: String?, pageNumber: Int, size: Int): DishSearchResponse? {
        if ((menuItemIds == null || menuItemIds.isEmpty()) && keyword.isNullOrBlank()) return DishSearchResponse(emptyList(), 0)
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val (items, total) = if (pageNumber <= 0 || size <= 0) {
            if (!menuItemIds.isNullOrEmpty()) {
                val list = dishRepository.findAllByMenuItemIds(menuItemIds)
                list to list.size.toLong()
            } else {
                val list = dishRepository.findAllByMenuItemName(keyword!!.trim())
                list to list.size.toLong()
            }
        } else {
            val pageable = PageRequest.of(max(pageNumber, 1) - 1, max(size, 1), sort)
            val page = if (!menuItemIds.isNullOrEmpty())
                dishRepository.findAllByMenuItemIds(menuItemIds, pageable)
            else
                dishRepository.findAllByMenuItemName(keyword!!.trim(), pageable)
            if (page.isEmpty) return DishSearchResponse(emptyList(), 0) else page.content to page.totalElements
        }
        if (items.isEmpty()) return DishSearchResponse(emptyList(), 0)
        return DishSearchResponse(mapDishResponses(items), total)
    }

    override fun searchByCalories(minCal: Int?, maxCal: Int?, pageNumber: Int, size: Int): DishSearchResponse? {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val min = minCal ?: 0
        val maxValue = maxCal ?: Int.MAX_VALUE
        val (items, total) = if (pageNumber <= 0 || size <= 0) {
            when {
                minCal != null && maxCal != null -> dishRepository.findAllByIsCustomFalseAndCalBetween(min, maxValue, sort)
                minCal != null -> dishRepository.findAllByIsCustomFalseAndCalGreaterThanEqual(min, sort)
                maxCal != null -> dishRepository.findAllByIsCustomFalseAndCalLessThanEqual(maxValue, sort)
                else -> dishRepository.findAllByIsCustomFalse(sort)
            }
            .let { it to it.size.toLong() }
        } else {
            val pageable = PageRequest.of(max(pageNumber, 1) - 1, max(size, 1), sort)
            val page = when {
                minCal != null && maxCal != null -> dishRepository.findAllByIsCustomFalseAndCalBetween(min, maxValue, pageable)
                minCal != null -> dishRepository.findAllByIsCustomFalseAndCalGreaterThanEqual(min, pageable)
                maxCal != null -> dishRepository.findAllByIsCustomFalseAndCalLessThanEqual(maxValue, pageable)
                else -> dishRepository.findAllByIsCustomFalse(pageable)
            }
            if (page.isEmpty) return DishSearchResponse(emptyList(), 0) else page.content to page.totalElements
        }
        if (items.isEmpty()) return DishSearchResponse(emptyList(), 0)
        return DishSearchResponse(mapDishResponses(items), total)
    }

    override fun searchByPrice(minPrice: Int?, maxPrice: Int?, pageNumber: Int, size: Int): DishSearchResponse? {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val min = minPrice ?: 0
        val maxValue = maxPrice ?: Int.MAX_VALUE
        val (items, total) = if (pageNumber <= 0 || size <= 0) {
            when {
                minPrice != null && maxPrice != null -> dishRepository.findAllByIsCustomFalseAndPriceBetween(min, maxValue, sort)
                minPrice != null -> dishRepository.findAllByIsCustomFalseAndPriceGreaterThanEqual(min, sort)
                maxPrice != null -> dishRepository.findAllByIsCustomFalseAndPriceLessThanEqual(maxValue, sort)
                else -> dishRepository.findAllByIsCustomFalse(sort)
            }
            .let { it to it.size.toLong() }
        } else {
            val pageable = PageRequest.of(max(pageNumber, 1) - 1, max(size, 1), sort)
            val page = when {
                minPrice != null && maxPrice != null -> dishRepository.findAllByIsCustomFalseAndPriceBetween(min, maxValue, pageable)
                minPrice != null -> dishRepository.findAllByIsCustomFalseAndPriceGreaterThanEqual(min, pageable)
                maxPrice != null -> dishRepository.findAllByIsCustomFalseAndPriceLessThanEqual(maxValue, pageable)
                else -> dishRepository.findAllByIsCustomFalse(pageable)
            }
            if (page.isEmpty) return DishSearchResponse(emptyList(), 0) else page.content to page.totalElements
        }
        if (items.isEmpty()) return DishSearchResponse(emptyList(), 0)
        return DishSearchResponse(mapDishResponses(items), total)
    }

    override fun search(
        menuItemIds: List<Long>?,
        keyword: String?,
        minCal: Int?,
        maxCal: Int?,
        minPrice: Int?,
        maxPrice: Int?,
        pageNumber: Int,
        size: Int,
    ): DishSearchResponse? {
        val keywordNormalized = keyword?.trim()?.lowercase()?.takeIf { it.isNotBlank() }
        val spec = Specification<Dish> { root, query, cb ->
            val preds = mutableListOf<jakarta.persistence.criteria.Predicate>()
            preds.add(cb.isFalse(root.get("isCustom")))

            var menuItemJoin: jakarta.persistence.criteria.Join<*, *>? = null
            if (!menuItemIds.isNullOrEmpty() || keywordNormalized != null) {
                val orderStepJoin = root.join<Any, Any>("orderSteps", JoinType.LEFT)
                val itemJoin = orderStepJoin.join<Any, Any>("items", JoinType.LEFT)
                menuItemJoin = itemJoin.join<Any, Any>("menuItem", JoinType.LEFT)
                query?.distinct(true)
            }

            if (!menuItemIds.isNullOrEmpty() && menuItemJoin != null) {
                preds.add(menuItemJoin.get<Long>("id").`in`(menuItemIds))
            }

            if (keywordNormalized != null) {
                val likePattern = "%$keywordNormalized%"
                val dishNamePredicate = cb.like(cb.lower(root.get("name")), likePattern)
                val menuItemPredicate = menuItemJoin?.let { cb.like(cb.lower(it.get("name")), likePattern) }
                preds.add(menuItemPredicate?.let { cb.or(dishNamePredicate, it) } ?: dishNamePredicate)
            }

            if (minCal != null) preds.add(cb.greaterThanOrEqualTo(root.get("cal"), minCal))
            if (maxCal != null) preds.add(cb.lessThanOrEqualTo(root.get("cal"), maxCal))

            if (minPrice != null) preds.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice))
            if (maxPrice != null) preds.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice))

            if (preds.isNotEmpty()) query?.where(*preds.toTypedArray())
            null
        }

        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        return if (pageNumber <= 0 || size <= 0) {
            val list = dishRepository.findAll(spec, sort)
            if (list.isEmpty()) DishSearchResponse(emptyList(), 0)
            else DishSearchResponse(mapDishResponses(list), list.size.toLong())
        } else {
            val pageable = PageRequest.of(max(pageNumber, 1) - 1, max(size, 1), sort)
            val page = dishRepository.findAll(spec, pageable)
            if (page.isEmpty) DishSearchResponse(emptyList(), 0)
            else DishSearchResponse(mapDishResponses(page.content), page.totalElements)
        }
    }

    private fun mapDishResponses(dishes: List<Dish>): List<DishResponse> =
        dishes.map { dish ->
            val nutrients = dishNutritionCacheService.ensureSnapshot(dish)
            dish.toDishResponse(nutrients)
        }
}
