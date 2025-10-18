package com.ChickenKitchen.app.service.menu

import com.ChickenKitchen.app.model.dto.request.CreateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.response.DailyMenuResponse
import com.ChickenKitchen.app.service.BaseService

interface DailyMenuService : BaseService<DailyMenuResponse, DailyMenuResponse, CreateDailyMenuRequest, UpdateDailyMenuRequest, Long> {

}