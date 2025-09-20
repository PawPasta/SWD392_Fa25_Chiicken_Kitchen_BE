package com.ChickenKitchen.app.service.cooking

import com.ChickenKitchen.app.model.entity.cooking.CookingMethod
import com.ChickenKitchen.app.model.dto.request.CreateCookingMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCookingMethodRequest
import com.ChickenKitchen.app.model.dto.response.CookingMethodResponse
import com.ChickenKitchen.app.model.dto.response.CookingMethodDetailResponse
import com.ChickenKitchen.app.service.BaseService

interface CookingMethodService : BaseService<
    CookingMethodResponse,
    CookingMethodDetailResponse,
    CreateCookingMethodRequest,
    UpdateCookingMethodRequest,
    Long
> {
    
}
