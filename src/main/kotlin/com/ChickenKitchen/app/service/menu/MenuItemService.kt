package com.ChickenKitchen.app.service.menu

import com.ChickenKitchen.app.model.dto.request.CreateMenuItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateMenuItemRequest
import com.ChickenKitchen.app.model.dto.response.MenuItemResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemDetailResponse
import com.ChickenKitchen.app.service.BaseService

interface MenuItemService : BaseService<MenuItemResponse, MenuItemDetailResponse, CreateMenuItemRequest, UpdateMenuItemRequest, Long> {
    fun changeStatus(id: Long): MenuItemResponse
}

