package com.ChickenKitchen.app.service.ingredient

import com.ChickenKitchen.app.model.dto.request.CreateIngredientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateIngredientRequest
import com.ChickenKitchen.app.model.dto.response.IngredientDetailResponse
import com.ChickenKitchen.app.model.dto.response.IngredientResponse
import com.ChickenKitchen.app.service.BaseService


interface IngredientService  : BaseService<IngredientResponse, IngredientDetailResponse, CreateIngredientRequest, UpdateIngredientRequest, Long>{

    fun changeStatus (id : Long) : IngredientDetailResponse
}