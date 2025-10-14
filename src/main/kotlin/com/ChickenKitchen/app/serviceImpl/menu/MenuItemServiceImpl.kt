package com.ChickenKitchen.app.serviceImpl.menu

import com.ChickenKitchen.app.model.dto.request.CreateMenuItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateMenuItemRequest
import com.ChickenKitchen.app.model.dto.response.MenuItemDetailResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemResponse
import com.ChickenKitchen.app.model.entity.menu.MenuItem
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.service.menu.MenuItemService
import com.ChickenKitchen.app.mapper.toMenuItemDetailResponse
import com.ChickenKitchen.app.mapper.toMenuItemResponse
import com.ChickenKitchen.app.mapper.toMenuItemResponseList
import org.springframework.stereotype.Service

@Service
class MenuItemServiceImpl(
    private val menuItemRepository: MenuItemRepository
) : MenuItemService {

    override fun getAll(): List<MenuItemResponse>? {
        val items = menuItemRepository.findAll()
        if (items.isEmpty()) return null
        return items.toMenuItemResponseList()
    }

    override fun getById(id: Long): MenuItemDetailResponse {
        val item = menuItemRepository.findById(id).orElseThrow { NoSuchElementException("MenuItem with id $id not found") }
        return item.toMenuItemDetailResponse()
    }

    override fun create(req: CreateMenuItemRequest): MenuItemDetailResponse {
        val entity = MenuItem(
            name = req.name,
            category = req.category,
            isActive = req.isActive,
            imageUrl = req.imageUrl,
        )
        val saved = menuItemRepository.save(entity)
        return saved.toMenuItemDetailResponse()
    }

    override fun update(id: Long, req: UpdateMenuItemRequest): MenuItemDetailResponse {
        val item = menuItemRepository.findById(id).orElseThrow { NoSuchElementException("MenuItem with id $id not found") }
        if (req.isActive != null) item.isActive = req.isActive
        if (req.imageUrl != null) item.imageUrl = req.imageUrl
        val updated = menuItemRepository.save(item)
        return updated.toMenuItemDetailResponse()
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
}
