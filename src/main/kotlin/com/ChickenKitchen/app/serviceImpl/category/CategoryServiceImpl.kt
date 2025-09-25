package com.ChickenKitchen.app.serviceImpl.category

import com.ChickenKitchen.app.handler.IngredientAlreadyExistsException
import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.mapper.toCategoryDetailResponse
import com.ChickenKitchen.app.mapper.toCategoryResponse
import com.ChickenKitchen.app.mapper.toCategoryResponseList
import com.ChickenKitchen.app.model.dto.request.CreateCategoryRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCategoryRequest
import com.ChickenKitchen.app.model.dto.response.CategoryDetailResponse
import com.ChickenKitchen.app.model.dto.response.CategoryResponse
import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.repository.category.CategoryRepository
import com.ChickenKitchen.app.service.category.CategoryService
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {

    override fun getAll(): List<CategoryResponse>? {
        val list = categoryRepository.findAll()
        if (list.isEmpty()) return null
        return list.toCategoryResponseList()
    }

    override fun getById(id: Long): CategoryDetailResponse {
        val category = categoryRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Category with id $id not found")
        return category.toCategoryDetailResponse()
    }

    override fun create(req: CreateCategoryRequest): CategoryDetailResponse {
        if (categoryRepository.findByName(req.name).isPresent) {
            throw IngredientAlreadyExistsException("Category with name ${req.name} already exists")
        }

        val newCategory = categoryRepository.save(
            Category(
                name = req.name,
                description = req.description,
                isActive = req.isActive
            )
        )
        return newCategory.toCategoryDetailResponse()
    }

    override fun update(id: Long, req: UpdateCategoryRequest): CategoryDetailResponse {
        val category = categoryRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Category with id $id not found")

        req.name?.let {
            if (it != category.name && categoryRepository.findByName(it).isPresent) {
                throw IngredientAlreadyExistsException("Category with name $it already exists")
            }
            category.name = it
        }
        req.description?.let { category.description = it }
        req.isActive?.let { category.isActive = it }

        val updated = categoryRepository.save(category)
        return updated.toCategoryDetailResponse()
    }

    override fun delete(id: Long) {
        val category = categoryRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Category with id $id not found")
        categoryRepository.delete(category)
    }

    override fun changeStatus(id: Long): CategoryResponse {
        val category = categoryRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Category with id $id not found")
        category.isActive = !category.isActive
        val updated = categoryRepository.save(category)
        return updated.toCategoryResponse()
    }
}

