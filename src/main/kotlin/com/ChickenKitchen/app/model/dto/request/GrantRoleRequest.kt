package com.ChickenKitchen.app.model.dto.request

import com.ChickenKitchen.app.enums.Role

data class GrantRoleRequest(
    val email: String,
    val role: Role,
    val fullName: String? = null
)

