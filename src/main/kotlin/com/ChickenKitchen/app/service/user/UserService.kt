package com.ChickenKitchen.app.service.user


import com.ChickenKitchen.app.model.dto.request.CreateUserRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserProfileRequest
import com.ChickenKitchen.app.model.dto.response.UserResponse
import com.ChickenKitchen.app.model.dto.response.UserDetailResponse
import com.ChickenKitchen.app.model.dto.response.UserProfileResponse
import com.ChickenKitchen.app.service.BaseService

interface UserService : BaseService<UserResponse, UserDetailResponse, CreateUserRequest, UpdateUserRequest, Long> {
    fun changeStatus(id: Long): UserResponse
    fun getProfile(): UserProfileResponse
    fun updateProfile(req: UpdateUserProfileRequest): UserProfileResponse
    // fun findByEmail(email: String): User?
    // fun changeRole(userId: Long, newRole: String): User
}
