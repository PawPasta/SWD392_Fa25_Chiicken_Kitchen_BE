package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enum.Role
import java.time.LocalDate

// Những response này chỉ dùng cho admin

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: Role = Role.USER,
    val isActive: Boolean = true,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthday: LocalDate? = null,
)

data class UpdateUserRequest(
    val username: String? = null,
    val email: String? = null,
    val role: Role? = null,
    val isActive: Boolean? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthday: LocalDate? = null,
)

// Những reponse này chỉ dùng cho user

data class UpdateUserProfileRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val birthday: LocalDate? = null,
)