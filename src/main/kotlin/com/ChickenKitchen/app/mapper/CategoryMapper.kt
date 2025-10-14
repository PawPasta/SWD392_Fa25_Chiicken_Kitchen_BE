package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.CategoryResponse
import com.ChickenKitchen.app.model.entity.category.Category


fun Category.toCategoryResponse() : CategoryResponse =
    CategoryResponse(
        id = this.id!!,
        name = this.name,
        description = this.description
    )


fun List<Category>.toCategoryResponseList() : List<CategoryResponse> = this.map { it.toCategoryResponse() }