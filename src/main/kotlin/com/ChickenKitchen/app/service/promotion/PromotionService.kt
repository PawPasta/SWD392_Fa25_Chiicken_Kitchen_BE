package com.ChickenKitchen.app.service.promotion

import com.ChickenKitchen.app.model.dto.request.CreatePromotionRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePromotionRequest
import com.ChickenKitchen.app.model.dto.response.PromotionDetailResponse
import com.ChickenKitchen.app.model.dto.response.PromotionResponse
import com.ChickenKitchen.app.service.BaseService

interface PromotionService : BaseService<PromotionResponse, PromotionDetailResponse,
        CreatePromotionRequest, UpdatePromotionRequest, Long> {
    fun changeStatus(id: Long): PromotionResponse
}

