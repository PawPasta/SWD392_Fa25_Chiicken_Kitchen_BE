package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enum.Role
import java.time.LocalDate

// Những response này chỉ dùng cho admin

data class UserResponse ( // Vốn dĩ nên dể cho UserDetailResponse kế thừa thuộc tính từ UserResponse 
    val id: Long,         //nhưng do Kotlin không hỗ trợ kế thừa thuộc tính data class nên đành phải viết lại
    val username: String,
    val email: String,
    val roles: Role,
    val isActive: Boolean,
)

data class UserDetailResponse(
    val id: Long,
    val username: String,
    val email: String,
    val roles: Role,
    val isActive: Boolean,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthday: LocalDate? = null,
    val createdAt: String,
    val updatedAt: String,
) 

// Những reponse này chỉ dùng cho user

data class UserProfileResponse(
    val username: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthday: LocalDate? = null,
    val createdAt: String,
)