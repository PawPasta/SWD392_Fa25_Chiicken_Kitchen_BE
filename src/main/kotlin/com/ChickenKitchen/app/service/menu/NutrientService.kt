package com.ChickenKitchen.app.service.menu

import com.ChickenKitchen.app.model.dto.request.CreateNutrientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateNutrientRequest
import com.ChickenKitchen.app.model.dto.response.NutrientResponse
import com.ChickenKitchen.app.model.dto.response.NutrientDetailResponse
import com.ChickenKitchen.app.service.BaseService

interface NutrientService : BaseService<NutrientResponse, NutrientDetailResponse, CreateNutrientRequest, UpdateNutrientRequest, Long>

