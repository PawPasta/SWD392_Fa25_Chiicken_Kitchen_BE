package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enums.Role
import java.time.LocalDate

// Những response này chỉ dùng cho admin

data class UserResponse ( // Vốn dĩ nên dể cho UserDetailResponse kế thừa thuộc tính từ UserResponse
    val id: Long,         //nhưng do Kotlin không hỗ trợ kế thừa thuộc tính data class nên đành phải viết lại
    val fullName: String,
    val email: String,
    val roles: Role,
    val isActive: Boolean,
    val imageURL: String?
)

data class UserDetailResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val roles: Role,
    val isActive: Boolean,
    val birthday: LocalDate? = null,
    val createdAt: String,
    val updatedAt: String,
    val imageURL : String? = null,
) 

// Những reponse này chỉ dùng cho user

data class UserProfileResponse(
    val fullName: String,
    val email: String,
    val birthday: LocalDate? = null,
    val createdAt: String,
    val imageURL : String? = null,
)

data class UserWalletResponse(
    val balance: Int,
    val transactions: List<TransactionResponse>,
)