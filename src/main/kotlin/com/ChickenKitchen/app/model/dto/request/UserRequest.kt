package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enums.Role
import java.time.LocalDate

// Những response này chỉ dùng cho admin

data class CreateUserRequest(
    val fullName: String,
    val email: String,
    val role: Role,
    val isActive: Boolean = true,
    val birthday: LocalDate? = null,
    val imageURL : String? = null,
)

data class UpdateUserRequest(
    val fullName: String? = null,
    val role: Role? = null,
    val isActive: Boolean? = null,
    val birthday: LocalDate? = null,
    val imageURL : String? = null,
)

// Những reponse này chỉ dùng cho user
data class UpdateUserProfileRequest(
    val fullName: String? = null,
    val birthday: LocalDate? = null,
    val imageURL : String? = null,
)
