package com.ChickenKitchen.app.serviceImpl.menu

import com.ChickenKitchen.app.model.dto.request.CreateMenuItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateMenuItemRequest
import com.ChickenKitchen.app.model.dto.response.MenuItemDetailResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemResponse
import com.ChickenKitchen.app.model.entity.menu.MenuItem
import com.ChickenKitchen.app.model.entity.menu.MenuItemNutrient
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.repository.menu.MenuItemNutrientRepository
import com.ChickenKitchen.app.repository.menu.NutrientRepository
import com.ChickenKitchen.app.repository.category.CategoryRepository
import com.ChickenKitchen.app.service.menu.MenuItemService
import com.ChickenKitchen.app.mapper.toMenuItemDetailResponse
import com.ChickenKitchen.app.mapper.toMenuItemResponse
import com.ChickenKitchen.app.mapper.toMenuItemResponseList
import com.ChickenKitchen.app.mapper.toBriefResponses
import org.springframework.stereotype.Service

@Service
class MenuItemServiceImpl(
    private val menuItemRepository: MenuItemRepository,
    private val menuItemNutrientRepository: MenuItemNutrientRepository,
    private val nutrientRepository: NutrientRepository,
    private val categoryRepository: CategoryRepository,
) : MenuItemService {

    override fun getAll(): List<MenuItemResponse>? {
        val items = menuItemRepository.findAll()
        if (items.isEmpty()) return null
        return items.toMenuItemResponseList()
    }

    override fun getById(id: Long): MenuItemDetailResponse {
        val item = menuItemRepository.findById(id).orElseThrow { NoSuchElementException("MenuItem with id $id not found") }
        return buildDetail(item)
    }

    override fun create(req: CreateMenuItemRequest): MenuItemDetailResponse {
        val category = categoryRepository.findById(req.categoryId)
            .orElseThrow { NoSuchElementException("Category with id ${req.categoryId} not found") }
        val entity = MenuItem(
            name = req.name,
            category = category,
            isActive = req.isActive,
            imageUrl = req.imageUrl,
        )
        val saved = menuItemRepository.save(entity)

        req.nutrients?.let { inputs ->
            if (inputs.isNotEmpty()) {
                val links = inputs.map { input ->
                    val nutrient = nutrientRepository.findById(input.nutrientId)
                        .orElseThrow { NoSuchElementException("Nutrient with id ${input.nutrientId} not found") }
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

    override fun update(id: Long, req: UpdateMenuItemRequest): MenuItemDetailResponse {
        var item = menuItemRepository.findById(id).orElseThrow { NoSuchElementException("MenuItem with id $id not found") }
        if (req.isActive != null) item.isActive = req.isActive
        if (req.imageUrl != null) item.imageUrl = req.imageUrl
        if (req.categoryId != null) {
            val newCategory = categoryRepository.findById(req.categoryId)
                .orElseThrow { NoSuchElementException("Category with id ${req.categoryId} not found") }
            // Create a new entity instance since category is val
            val replaced = MenuItem(
                id = item.id,
                name = item.name,
                category = newCategory,
                isActive = item.isActive,
                imageUrl = item.imageUrl,
                createdAt = item.createdAt,
                // stepItems = item.stepItems,
                dailyMenuItems = item.dailyMenuItems,
                recipes = item.recipes,
            )
            // Persist replaced entity
            val saved = menuItemRepository.save(replaced)
            // Continue with saved for subsequent updates
            item = saved
        }
        val updated = menuItemRepository.save(item)

        if (req.nutrients != null) {
            // Replace existing nutrients with provided list
            val existing = menuItemNutrientRepository.findByMenuItemId(id)
            if (existing.isNotEmpty()) menuItemNutrientRepository.deleteAll(existing)

            val newLinks = req.nutrients!!.map { input ->
                val nutrient = nutrientRepository.findById(input.nutrientId)
                    .orElseThrow { NoSuchElementException("Nutrient with id ${input.nutrientId} not found") }
                MenuItemNutrient(
                    menuItem = updated,
                    nutrient = nutrient,
                    quantity = input.quantity,
                )
            }
            if (newLinks.isNotEmpty()) menuItemNutrientRepository.saveAll(newLinks)
        }

        return buildDetail(updated)
    }

    override fun delete(id: Long) {
        val item = menuItemRepository.findById(id).orElseThrow { NoSuchElementException("MenuItem with id $id not found") }
        menuItemRepository.delete(item)
    }

    override fun changeStatus(id: Long): MenuItemResponse {
        val item = menuItemRepository.findById(id).orElseThrow { NoSuchElementException("MenuItem with id $id not found") }
        item.isActive = !item.isActive
        val saved = menuItemRepository.save(item)
        return saved.toMenuItemResponse()
    }

    private fun buildDetail(item: MenuItem): MenuItemDetailResponse {
        val links = menuItemNutrientRepository.findByMenuItemId(item.id!!)
        val brief = links.toBriefResponses()
        return item.toMenuItemDetailResponse(brief)
    }
}
