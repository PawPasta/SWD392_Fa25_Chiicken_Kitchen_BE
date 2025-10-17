package com.ChickenKitchen.app.service.ingredient

import com.ChickenKitchen.app.model.dto.request.CreateStoreRequest
import com.ChickenKitchen.app.model.dto.request.UpdateStoreRequest
import com.ChickenKitchen.app.model.dto.response.StoreResponse
import com.ChickenKitchen.app.service.BaseService


interface StoreService : BaseService<StoreResponse, StoreResponse, CreateStoreRequest, UpdateStoreRequest,Long>{

    fun changeStatus (id : Long) : StoreResponse
}