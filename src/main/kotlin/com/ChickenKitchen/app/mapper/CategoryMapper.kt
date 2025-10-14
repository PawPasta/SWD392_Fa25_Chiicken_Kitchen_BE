package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.model.dto.response.CategoryResponse
import com.ChickenKitchen.app.model.dto.response.CategoryDetailResponse

fun Category.toCategoryResponse(): CategoryResponse =
    CategoryResponse(
        id = this.id!!,
        name = this.name,
        description = this.description,
    )

fun List<Category>.toCategoryResponseList(): List<CategoryResponse> =
    this.map { it.toCategoryResponse() }

fun Category.toCategoryDetailResponse(): CategoryDetailResponse =
    CategoryDetailResponse(
        id = this.id!!,
        name = this.name,
        description = this.description,
    )

