package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.CategoryDetailResponse
import com.ChickenKitchen.app.model.dto.response.CategoryResponse
import com.ChickenKitchen.app.model.entity.category.Category

fun Category.toCategoryResponse() = CategoryResponse(
    id = this.id!!,
    name = this.name,
    description = this.description,
    isActive = this.isActive
)

fun Category.toCategoryDetailResponse() = CategoryDetailResponse(
    id = this.id!!,
    name = this.name,
    description = this.description,
    isActive = this.isActive
)

fun List<Category>.toCategoryResponseList() = this.map { it.toCategoryResponse() }

