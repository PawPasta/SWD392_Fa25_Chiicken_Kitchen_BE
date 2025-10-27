package com.ChickenKitchen.app.serviceImpl.menu

import com.ChickenKitchen.app.handler.CategoryNotFoundException
import com.ChickenKitchen.app.handler.MenuItemHasOrdersException
import com.ChickenKitchen.app.handler.MenuItemHasRecipesException
import com.ChickenKitchen.app.handler.MenuItemNotFoundException
import com.ChickenKitchen.app.handler.MenuItemUsedInDailyMenuException
import com.ChickenKitchen.app.handler.NutrientNotFoundException
import com.ChickenKitchen.app.model.dto.request.CreateMenuItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateMenuItemRequest
import com.ChickenKitchen.app.model.dto.response.MenuItemDetailResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemResponse
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
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service

@Service
class MenuItemServiceImpl(
    private val menuItemRepository: MenuItemRepository,
    private val menuItemNutrientRepository: MenuItemNutrientRepository,
    private val nutrientRepository: NutrientRepository,
    private val categoryRepository: CategoryRepository,
    private val orderStepItemRepository: OrderStepItemRepository,
    private val recipeRepository: RecipeRepository
) : MenuItemService {

    @Cacheable(cacheNames = ["menuItemsAll"], unless = "#result == null")
    override fun getAll(): List<MenuItemResponse>? {
        val items = menuItemRepository.findAll()
        if (items.isEmpty()) return null
        return items.toMenuItemResponseList()
    }

    override fun getById(id: Long): MenuItemDetailResponse {
        val item = menuItemRepository.findById(id)
            .orElseThrow { MenuItemNotFoundException("MenuItem with id $id not found") }
        return buildDetail(item)
    }

    @Caching(
        evict = [
            CacheEvict(cacheNames = ["menuItemsAll"], allEntries = true),
            // Daily menu responses may include menu item fields; keep them in sync
            CacheEvict(cacheNames = ["dailyMenuByStoreDate"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuAll"], allEntries = true),
        ]
    )
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
            }
        }

        return buildDetail(saved)
    }

    @Caching(
        evict = [
            CacheEvict(cacheNames = ["menuItemsAll"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuByStoreDate"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuAll"], allEntries = true),
        ]
    )
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
            if (newLinks.isNotEmpty()) menuItemNutrientRepository.saveAll(newLinks)
        }

        return buildDetail(item)
    }

    @Caching(
        evict = [
            CacheEvict(cacheNames = ["menuItemsAll"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuByStoreDate"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuAll"], allEntries = true),
        ]
    )
    override fun delete(id: Long) {
        val item = menuItemRepository.findById(id)
            .orElseThrow { MenuItemNotFoundException("MenuItem with id $id not found") }

        val orderCount = orderStepItemRepository.countByDailyMenuItemMenuItemId(id)
        if (orderCount > 0) throw MenuItemHasOrdersException("Cannot delete MenuItem with id $id: it has $orderCount orders")

        if (item.dailyMenuItems.isNotEmpty()) {
            throw MenuItemUsedInDailyMenuException("Cannot delete MenuItem with id $id: it is used in ${item.dailyMenuItems.size} daily menus")
        }

        if (item.recipes.isNotEmpty()) {
            throw MenuItemHasRecipesException("Cannot delete MenuItem with id $id: it has ${item.recipes.size} recipes")
        }

        // Delete nutrients first
        val nutrients = menuItemNutrientRepository.findByMenuItemId(id)
        if (nutrients.isNotEmpty()) menuItemNutrientRepository.deleteAll(nutrients)

        menuItemRepository.delete(item)
    }

    @Caching(
        evict = [
            CacheEvict(cacheNames = ["menuItemsAll"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuByStoreDate"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuAll"], allEntries = true),
        ]
    )
    override fun changeStatus(id: Long): MenuItemResponse {
        val item = menuItemRepository.findById(id)
            .orElseThrow { MenuItemNotFoundException("MenuItem with id $id not found") }
        item.isActive = !item.isActive
        val saved = menuItemRepository.save(item)
        return saved.toMenuItemResponse()
    }

    private fun buildDetail(item: MenuItem): MenuItemDetailResponse {
        val nutrientLinks = menuItemNutrientRepository.findByMenuItemId(item.id!!)
        val nutrientBriefs = nutrientLinks.toBriefResponses()

        val recipes = recipeRepository.findByMenuItemId(item.id!!)
        val recipeBriefs = recipes.toRecipeBriefResponse()

        return item.toMenuItemDetailResponse(
            nutrients = nutrientBriefs,
            recipes = recipeBriefs
        )
    }
}
