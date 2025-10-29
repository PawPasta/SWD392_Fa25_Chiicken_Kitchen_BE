package com.ChickenKitchen.app.service.user


import com.ChickenKitchen.app.model.dto.request.CreateUserRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserProfileRequest
import com.ChickenKitchen.app.model.dto.response.UserResponse
import com.ChickenKitchen.app.model.dto.response.UserDetailResponse
import com.ChickenKitchen.app.model.dto.response.UserProfileResponse
import com.ChickenKitchen.app.model.dto.response.UserCountsResponse
import com.ChickenKitchen.app.service.BaseService
import com.ChickenKitchen.app.model.dto.request.GrantRoleRequest
import com.ChickenKitchen.app.enums.Role

interface UserService : BaseService<UserResponse, UserDetailResponse, CreateUserRequest, UpdateUserRequest, Long> {
    fun changeStatus(id: Long): UserResponse
    fun getProfile(): UserProfileResponse
    fun updateProfile(req: UpdateUserProfileRequest): UserProfileResponse
    fun grantRoleByEmail(req: GrantRoleRequest): UserDetailResponse
    fun getAllRoles(): List<Role>
    fun getAll(pageNumber: Int, size: Int): List<UserResponse>?
    fun getCounts(): UserCountsResponse
    // fun findByEmail(email: String): User?
    // fun changeRole(userId: Long, newRole: String): User
}
