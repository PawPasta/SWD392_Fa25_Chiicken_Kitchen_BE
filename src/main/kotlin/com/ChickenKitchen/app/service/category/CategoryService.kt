package com.ChickenKitchen.app.service.category

import com.ChickenKitchen.app.model.dto.request.CreateCategoryRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCategoryRequest
import com.ChickenKitchen.app.model.dto.response.CategoryResponse
import com.ChickenKitchen.app.model.dto.response.CategoryDetailResponse
import com.ChickenKitchen.app.service.BaseService

interface CategoryService : BaseService<CategoryResponse, CategoryDetailResponse, CreateCategoryRequest, UpdateCategoryRequest, Long>

