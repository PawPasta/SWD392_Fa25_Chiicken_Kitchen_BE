package com.ChickenKitchen.app.service.step

import com.ChickenKitchen.app.model.dto.request.CreateDishBaseRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishBaseRequest
import com.ChickenKitchen.app.model.dto.response.DishDetailResponse
import com.ChickenKitchen.app.model.dto.response.DishSearchResponse
import com.ChickenKitchen.app.model.dto.response.DishResponse
import com.ChickenKitchen.app.service.BaseService

interface DishService : BaseService<DishResponse, DishDetailResponse, CreateDishBaseRequest, UpdateDishBaseRequest, Long> {
    fun getAll(pageNumber: Int, size: Int): List<DishResponse>?
    fun count(): Long
    fun getMyCustomDishes(): List<DishResponse>?
    // Search sample dishes by their components (menu items)
    fun searchByComponents(menuItemIds: List<Long>?, keyword: String?, pageNumber: Int, size: Int): DishSearchResponse?
    fun searchByCalories(minCal: Int?, maxCal: Int?, pageNumber: Int, size: Int): DishSearchResponse?
    fun searchByPrice(minPrice: Int?, maxPrice: Int?, pageNumber: Int, size: Int): DishSearchResponse?
    fun search(
        menuItemIds: List<Long>?,
        keyword: String?,
        minCal: Int?,
        maxCal: Int?,
        minPrice: Int?,
        maxPrice: Int?,
        pageNumber: Int,
        size: Int,
    ): DishSearchResponse?
}
