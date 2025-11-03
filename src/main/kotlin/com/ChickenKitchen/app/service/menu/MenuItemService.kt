package com.ChickenKitchen.app.service.menu

import com.ChickenKitchen.app.model.dto.request.CreateMenuItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateMenuItemRequest
import com.ChickenKitchen.app.model.dto.response.MenuItemResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemDetailResponse
import com.ChickenKitchen.app.service.BaseService

interface MenuItemService : BaseService<MenuItemResponse, MenuItemDetailResponse, CreateMenuItemRequest, UpdateMenuItemRequest, Long> {
    fun changeStatus(id: Long): MenuItemResponse
    fun getAll(pageNumber: Int, size: Int): List<MenuItemResponse>?
    fun count(): Long
    fun search(name: String? = null, categoryId: Long? = null, sortBy: String = "name", direction: String = "asc"): List<MenuItemResponse>
    fun searchPaged(
        name: String? = null,
        categoryId: Long? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        minCal: Int? = null,
        maxCal: Int? = null,
        pageNumber: Int = 1,
        size: Int = 10,
        sortBy: String = "createdAt",
        direction: String = "desc"
    ): com.ChickenKitchen.app.model.dto.response.MenuItemSearchResponse?
}
