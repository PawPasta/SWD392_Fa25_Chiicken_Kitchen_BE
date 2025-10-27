package com.ChickenKitchen.app.serviceImpl.category

import com.ChickenKitchen.app.handler.CategoryHasMenuItemsException
import com.ChickenKitchen.app.handler.CategoryNameExistException
import com.ChickenKitchen.app.handler.CategoryNotFoundException
import com.ChickenKitchen.app.mapper.toCategoryDetailResponse

import com.ChickenKitchen.app.mapper.toCategoryResponseList
import com.ChickenKitchen.app.model.dto.request.CreateCategoryRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCategoryRequest
import com.ChickenKitchen.app.model.dto.response.CategoryDetailResponse
import com.ChickenKitchen.app.model.dto.response.CategoryResponse
import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.repository.category.CategoryRepository
import com.ChickenKitchen.app.service.category.CategoryService
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {

    @Cacheable(cacheNames = ["categoriesAll"], unless = "#result == null")
    override fun getAll(): List<CategoryResponse>? {
        val list = categoryRepository.findAll()
        if (list.isEmpty()) return null
        return list.toCategoryResponseList()
    }

    @Cacheable(cacheNames = ["categoryById"], key = "#id")
    override fun getById(id: Long): CategoryDetailResponse {
        val entity = categoryRepository.findById(id).orElseThrow { CategoryNotFoundException("Category with id $id not found") }
        return entity.toCategoryDetailResponse()
    }

    @Caching(
        evict = [
            CacheEvict(cacheNames = ["categoriesAll"], allEntries = true),
            CacheEvict(cacheNames = ["categoryById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuByStoreDate"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuAll"], allEntries = true),
        ]
    )
    override fun create(req: CreateCategoryRequest): CategoryDetailResponse {

        val current = categoryRepository.findByName(req.name)

        if (current != null) {
            throw CategoryNameExistException("Category name '${req.name}' already exists")
        }

        val entity = Category(
            name = req.name,
            description = req.description,
        )
        val saved = categoryRepository.save(entity)
        return saved.toCategoryDetailResponse()
    }

    @Caching(
        evict = [
            CacheEvict(cacheNames = ["categoriesAll"], allEntries = true),
            CacheEvict(cacheNames = ["categoryById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuByStoreDate"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuAll"], allEntries = true),
        ]
    )
    override fun update(id: Long, req: UpdateCategoryRequest): CategoryDetailResponse {
        val current = categoryRepository.findById(id).orElseThrow { CategoryNotFoundException("Category with id $id not found") }
        val updated = Category(
            id = current.id,
            name = req.name ?: current.name,
            description = req.description ?: current.description,
            steps = current.steps
        )
        val saved = categoryRepository.save(updated)
        return saved.toCategoryDetailResponse()
    }

    @Caching(
        evict = [
            CacheEvict(cacheNames = ["categoriesAll"], allEntries = true),
            CacheEvict(cacheNames = ["categoryById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuByStoreDate"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuById"], allEntries = true),
            CacheEvict(cacheNames = ["dailyMenuAll"], allEntries = true),
        ]
    )
    override fun delete(id: Long) {
        val entity = categoryRepository.findById(id).orElseThrow { CategoryNotFoundException("Category with id $id not found") }

        if (entity.menuItems.isNotEmpty()) {
            throw CategoryHasMenuItemsException("Category with id $id has menu items and cannot be deleted")
        }

        categoryRepository.delete(entity)
    }
}
