package com.ChickenKitchen.app.model.dto.response

import java.sql.Timestamp

data class EmployeeDetailFullResponse(
    val id: Long,
    val position: String?,
    val isActive: Boolean,
    val note: String?,
    val createdAt: Timestamp?,
    val updatedAt: Timestamp?,
    val store: StoreResponse,
    val user: UserResponse,
)

