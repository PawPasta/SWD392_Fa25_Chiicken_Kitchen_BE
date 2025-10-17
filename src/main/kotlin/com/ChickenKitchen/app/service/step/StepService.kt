package com.ChickenKitchen.app.service.step

import com.ChickenKitchen.app.model.dto.request.CreateStepRequest
import com.ChickenKitchen.app.model.dto.request.StepOrderRequest
import com.ChickenKitchen.app.model.dto.request.UpdateStepRequest
import com.ChickenKitchen.app.model.dto.response.StepDetailResponse
import com.ChickenKitchen.app.model.dto.response.StepResponse
import com.ChickenKitchen.app.service.BaseService

interface StepService : BaseService<StepResponse, StepDetailResponse, CreateStepRequest, UpdateStepRequest, Long> {
    fun changeOrder(id: Long, req: StepOrderRequest): StepResponse
}

