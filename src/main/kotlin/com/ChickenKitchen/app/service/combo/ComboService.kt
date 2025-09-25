package com.ChickenKitchen.app.service.combo

import com.ChickenKitchen.app.model.dto.request.CreateComboRequest
import com.ChickenKitchen.app.model.dto.request.UpdateComboRequest
import com.ChickenKitchen.app.model.dto.response.ComboDetailResponse
import com.ChickenKitchen.app.model.dto.response.ComboResponse
import com.ChickenKitchen.app.service.BaseService

interface ComboService : BaseService<ComboResponse, ComboDetailResponse,
        CreateComboRequest, UpdateComboRequest, Long> {
    fun changeStatus(id: Long): ComboResponse
}

