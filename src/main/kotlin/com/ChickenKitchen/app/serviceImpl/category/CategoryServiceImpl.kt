package com.ChickenKitchen.app.serviceImpl.category

import com.ChickenKitchen.app.mapper.toCategoryResponse
import com.ChickenKitchen.app.mapper.toCategoryResponseList
import com.ChickenKitchen.app.model.dto.request.CreateCategoryRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCategoryRequest
import com.ChickenKitchen.app.model.dto.response.CategoryResponse
import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.repository.category.CategoryRepository
import com.ChickenKitchen.app.service.category.CategoryService
import org.springframework.stereotype.Service


@Service
class CategoryServiceImpl (
    private val categoryRepository: CategoryRepository
): CategoryService {

    override fun getAll(): List<CategoryResponse>? {
        val list = categoryRepository.findAll()
        if(list.isEmpty()) return null
        return list.toCategoryResponseList()
    }

    override fun getById(id: Long): CategoryResponse {
        val category = categoryRepository.findById(id).orElseThrow {NoSuchElementException("Category with id $id not found")}
        return category.toCategoryResponse()
    }

    override fun create(req: CreateCategoryRequest): CategoryResponse {

        val savedEntity = categoryRepository.save( Category (
            name = req.name,
            description = req.description,
        ))

        return CategoryResponse (
            id = savedEntity.id,
            name = savedEntity.name,
            description = savedEntity.description
        )
    }

    override fun update(
        id: Long,
        req: UpdateCategoryRequest
    ): CategoryResponse {
       val saveEntity = categoryRepository.findById(id).orElseThrow { NoSuchElementException("Category with id $id not found")}

        if(req.name != null) saveEntity.name = req.name;
        if(req.description != null) saveEntity.description = req.description;

        val update = categoryRepository.save(saveEntity)

        return CategoryResponse(
            id = update.id,
            name = update.name,
            description = update.description

        )

    }
    // M khoải, cái này chưa dùng, cập nhật logic sau vì còn dính Steps
    override fun delete(id: Long) {
        val deleteEntity = categoryRepository.findById(id).orElseThrow { NoSuchElementException("Category with id $id not found")}
        categoryRepository.delete(deleteEntity)
    }
}