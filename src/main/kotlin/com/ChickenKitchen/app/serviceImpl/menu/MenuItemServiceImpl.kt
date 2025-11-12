package com.ChickenKitchen.app.serviceImpl.menu

import com.ChickenKitchen.app.handler.CategoryNotFoundException
import com.ChickenKitchen.app.handler.MenuItemHasOrdersException
import com.ChickenKitchen.app.handler.MenuItemHasRecipesException
import com.ChickenKitchen.app.handler.MenuItemNotFoundException
import com.ChickenKitchen.app.handler.NutrientNotFoundException
import com.ChickenKitchen.app.model.dto.request.CreateMenuItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateMenuItemRequest
import com.ChickenKitchen.app.model.dto.response.MenuItemDetailResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemSearchResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemNutrientBriefResponse
import com.ChickenKitchen.app.model.entity.menu.MenuItem
import com.ChickenKitchen.app.model.entity.menu.MenuItemNutrient
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.repository.order.OrderStepItemRepository
import com.ChickenKitchen.app.repository.menu.MenuItemNutrientRepository
import com.ChickenKitchen.app.repository.menu.NutrientRepository
import com.ChickenKitchen.app.repository.category.CategoryRepository
import com.ChickenKitchen.app.service.menu.MenuItemService
import com.ChickenKitchen.app.mapper.toMenuItemDetailResponse
import com.ChickenKitchen.app.mapper.toMenuItemResponse
import com.ChickenKitchen.app.mapper.toMenuItemResponseList
import com.ChickenKitchen.app.mapper.toBriefResponses
import com.ChickenKitchen.app.mapper.toRecipeBriefResponse
import com.ChickenKitchen.app.repository.ingredient.RecipeRepository
import com.ChickenKitchen.app.util.NutrientJsonUtil
import org.springframework.stereotype.Service
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import kotlin.math.max
import jakarta.persistence.criteria.Predicate

@Service
class MenuItemServiceImpl(
    private val menuItemRepository: MenuItemRepository,
    private val menuItemNutrientRepository: MenuItemNutrientRepository,
    private val nutrientRepository: NutrientRepository,
    private val categoryRepository: CategoryRepository,
    private val orderStepItemRepository: OrderStepItemRepository,
    private val recipeRepository: RecipeRepository
) : MenuItemService {

    override fun getAll(): List<MenuItemResponse>? {
        val items = menuItemRepository.findAll()
        if (items.isEmpty()) return null
        return items.toMenuItemResponseList()
    }

    override fun getAll(pageNumber: Int, size: Int): List<MenuItemResponse>? {
        val safeSize = max(size, 1)
        val safePage = max(pageNumber, 1)
        val pageable = PageRequest.of(safePage - 1, safeSize)
        val page = menuItemRepository.findAll(pageable)
        if (page.isEmpty) return null
        return page.content.toMenuItemResponseList()
    }

    override fun getById(id: Long): MenuItemDetailResponse {
        val item = menuItemRepository.findById(id)
            .orElseThrow { MenuItemNotFoundException("MenuItem with id $id not found") }
        return buildDetail(item)
    }

    override fun create(req: CreateMenuItemRequest): MenuItemDetailResponse {
        val category = categoryRepository.findById(req.categoryId)
            .orElseThrow { CategoryNotFoundException("Category with id ${req.categoryId} not found") }

        val entity = MenuItem(
            name = req.name,
            category = category,
            isActive = req.isActive,
            imageUrl = req.imageUrl,
            price = req.price,
            cal = req.cal,
            description = req.description
        )
        val saved = menuItemRepository.save(entity)

        req.nutrients?.let { inputs ->
            if (inputs.isNotEmpty()) {
                val links = inputs.map { input ->
                    val nutrient = nutrientRepository.findById(input.nutrientId)
                        .orElseThrow { NutrientNotFoundException("Nutrient with id ${input.nutrientId} not found") }

                    MenuItemNutrient(
                        menuItem = saved,
                        nutrient = nutrient,
                        quantity = input.quantity,
                    )
                }
                menuItemNutrientRepository.saveAll(links)
                refreshMenuItemNutrientCache(saved, links)
            } else {
                refreshMenuItemNutrientCache(saved, emptyList())
            }
        } ?: refreshMenuItemNutrientCache(saved)

        return buildDetail(saved)
    }

    override fun update(id: Long, req: UpdateMenuItemRequest): MenuItemDetailResponse {
        val item = menuItemRepository.findById(id)
            .orElseThrow { MenuItemNotFoundException("MenuItem with id $id not found") }

        // Update simple fields
        req.isActive?.let { item.isActive = it }
        req.imageUrl?.let { item.imageUrl = it }
        req.price?.let { item.price = it }
        req.cal?.let { item.cal = it }
        req.description?.let { item.description = it }

        // Update category if provided
        if (req.categoryId != null && req.categoryId != item.category.id) {
            val newCategory = categoryRepository.findById(req.categoryId)
                .orElseThrow { CategoryNotFoundException("Category with id ${req.categoryId} not found") }
            val updatedItem = item.copy(category = newCategory)
            menuItemRepository.save(updatedItem)
        } else {
            menuItemRepository.save(item)
        }

        // Update nutrients
        req.nutrients?.let { inputs ->
            val existing = menuItemNutrientRepository.findByMenuItemId(id)
            if (existing.isNotEmpty()) menuItemNutrientRepository.deleteAll(existing)

            val newLinks = inputs.map { input ->
                val nutrient = nutrientRepository.findById(input.nutrientId)
                    .orElseThrow { NutrientNotFoundException("Nutrient with id ${input.nutrientId} not found") }
                MenuItemNutrient(menuItem = item, nutrient = nutrient, quantity = input.quantity)
            }
            if (newLinks.isNotEmpty()) {
                menuItemNutrientRepository.saveAll(newLinks)
            }
            refreshMenuItemNutrientCache(item, newLinks)
        }

        return buildDetail(item)
    }

    override fun delete(id: Long) {
        val item = menuItemRepository.findById(id)
            .orElseThrow { MenuItemNotFoundException("MenuItem with id $id not found") }

        val orderCount = orderStepItemRepository.countByMenuItemId(id)
        if (orderCount > 0) throw MenuItemHasOrdersException("Cannot delete MenuItem with id $id: it has $orderCount orders")

        if (item.recipes.isNotEmpty()) {
            throw MenuItemHasRecipesException("Cannot delete MenuItem with id $id: it has ${item.recipes.size} recipes")
        }

        // Delete nutrients first
        val nutrients = menuItemNutrientRepository.findByMenuItemId(id)
        if (nutrients.isNotEmpty()) menuItemNutrientRepository.deleteAll(nutrients)

        menuItemRepository.delete(item)
    }

    override fun changeStatus(id: Long): MenuItemResponse {
        val item = menuItemRepository.findById(id)
            .orElseThrow { MenuItemNotFoundException("MenuItem with id $id not found") }
        item.isActive = !item.isActive
        val saved = menuItemRepository.save(item)
        return saved.toMenuItemResponse()
    }

    override fun count(): Long = menuItemRepository.count()

    private fun buildDetail(item: MenuItem): MenuItemDetailResponse {
        val nutrientBriefs = getCachedMenuItemNutrients(item)
        val recipeBriefs = recipeRepository.findByMenuItemId(item.id!!).toRecipeBriefResponse()

        return item.toMenuItemDetailResponse(
            nutrients = nutrientBriefs,
            recipes = recipeBriefs
        )
    }

    private fun getCachedMenuItemNutrients(item: MenuItem): List<MenuItemNutrientBriefResponse> {
        val cached = NutrientJsonUtil.parse(item.nutrientJson)
        return if (cached.isNotEmpty()) cached else refreshMenuItemNutrientCache(item)
    }

    private fun refreshMenuItemNutrientCache(
        item: MenuItem,
        links: List<MenuItemNutrient>? = null
    ): List<MenuItemNutrientBriefResponse> {
        val source = links ?: run {
            val id = item.id ?: return emptyList()
            menuItemNutrientRepository.findByMenuItemId(id)
        }
        val nutrientBriefs = source.toBriefResponses()
        item.nutrientJson = NutrientJsonUtil.toJson(nutrientBriefs)
        menuItemRepository.save(item)
        return nutrientBriefs
    }

    override fun search(name: String?, categoryId: Long?, sortBy: String, direction: String): List<MenuItemResponse> {
        val spec = Specification<MenuItem> { root, query, cb ->
            val preds = mutableListOf<Predicate>()
            if (!name.isNullOrBlank()) {
                preds.add(cb.like(cb.lower(root.get<String>("name")), "%${name.trim().lowercase()}%"))
            }
            if (categoryId != null) {
                preds.add(cb.equal(root.get<Any>("category").get<Long>("id"), categoryId))
            }
            if (preds.isNotEmpty()) {
                query?.where(*preds.toTypedArray())
            }
            null
        }

        val sort = if (direction.equals("desc", true)) Sort.by(sortBy).descending() else Sort.by(sortBy).ascending()
        return menuItemRepository.findAll(spec, sort).map { mi ->
            MenuItemResponse(
                id = mi.id!!,
                name = mi.name,
                categoryId = mi.category.id!!,
                categoryName = mi.category.name,
                isActive = mi.isActive,
                imageUrl = mi.imageUrl,
                price = mi.price,
                cal = mi.cal,
                description = mi.description
            )
        }
    }

    override fun searchPaged(
        name: String?,
        categoryId: Long?,
        minPrice: Int?,
        maxPrice: Int?,
        minCal: Int?,
        maxCal: Int?,
        pageNumber: Int,
        size: Int,
        sortBy: String,
        direction: String
    ): MenuItemSearchResponse? {
        val spec = Specification<MenuItem> { root, query, cb ->
            val preds = mutableListOf<Predicate>()
            if (!name.isNullOrBlank()) {
                preds.add(cb.like(cb.lower(root.get("name")), "%${name.trim().lowercase()}%"))
            }
            if (categoryId != null) {
                preds.add(cb.equal(root.get<Any>("category").get<Long>("id"), categoryId))
            }
            if (minPrice != null) {
                preds.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice))
            }
            if (maxPrice != null) {
                preds.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice))
            }
            if (minCal != null) {
                preds.add(cb.greaterThanOrEqualTo(root.get("cal"), minCal))
            }
            if (maxCal != null) {
                preds.add(cb.lessThanOrEqualTo(root.get("cal"), maxCal))
            }
            if (preds.isNotEmpty()) {
                query?.where(*preds.toTypedArray())
            }
            null
        }

        val sort = if (direction.equals("desc", true)) Sort.by(sortBy).descending() else Sort.by(sortBy).ascending()

        return if (pageNumber <= 0 || size <= 0) {
            val list = menuItemRepository.findAll(spec, sort)
            if (list.isEmpty()) MenuItemSearchResponse(emptyList(), 0)
            else MenuItemSearchResponse(list.map { it.toMenuItemResponse() }, list.size.toLong())
        } else {
            val pageable = PageRequest.of(kotlin.math.max(pageNumber, 1) - 1, kotlin.math.max(size, 1), sort)
            val page = menuItemRepository.findAll(spec, pageable)
            if (page.isEmpty) MenuItemSearchResponse(emptyList(), 0)
            else MenuItemSearchResponse(page.content.map { it.toMenuItemResponse() }, page.totalElements)
        }
    }
}
