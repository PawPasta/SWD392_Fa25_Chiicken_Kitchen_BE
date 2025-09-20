package com.ChickenKitchen.app.service.cooking

import com.ChickenKitchen.app.model.entity.cooking.CookingEffect
import com.ChickenKitchen.app.model.dto.request.CreateCookingEffectRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCookingEffectRequest
import com.ChickenKitchen.app.model.dto.response.CookingEffectResponse
import com.ChickenKitchen.app.model.dto.response.CookingEffectDetailResponse
import com.ChickenKitchen.app.service.BaseService

interface CookingEffectService : BaseService<
    CookingEffectResponse,
    CookingEffectDetailResponse,
    CreateCookingEffectRequest,
    UpdateCookingEffectRequest,
    Long
> {
    
}
