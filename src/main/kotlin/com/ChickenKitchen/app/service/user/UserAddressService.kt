package com.ChickenKitchen.app.service.user

import org.springframework.stereotype.Service
import com.ChickenKitchen.app.service.BaseService
import com.ChickenKitchen.app.model.entity.user.UserAddress
import com.ChickenKitchen.app.model.dto.response.UserAddressResponse
import com.ChickenKitchen.app.model.dto.request.CreateUserAddressRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserAddressRequest

interface UserAddressService : BaseService<UserAddressResponse, UserAddressResponse, 
CreateUserAddressRequest, UpdateUserAddressRequest, Long> {
    fun setDefault(id: Long)
    // fun getProvinces(): List<String>
    // fun getDistricts(province: String): List<String>
    // fun getWards(district: String): List<String>
}