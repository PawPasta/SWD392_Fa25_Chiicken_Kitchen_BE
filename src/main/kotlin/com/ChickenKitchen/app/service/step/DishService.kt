package com.ChickenKitchen.app.service.step

import com.ChickenKitchen.app.model.dto.request.CreateDishBaseRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishBaseRequest
import com.ChickenKitchen.app.model.dto.response.DishDetailResponse
import com.ChickenKitchen.app.model.dto.response.DishResponse
import com.ChickenKitchen.app.service.BaseService

interface DishService : BaseService<DishResponse, DishDetailResponse, CreateDishBaseRequest, UpdateDishBaseRequest, Long> {
    fun getAll(pageNumber: Int, size: Int): List<DishResponse>?
    fun count(): Long
}

