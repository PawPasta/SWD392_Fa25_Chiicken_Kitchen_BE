package com.ChickenKitchen.app.model.dto.response

import com.ChickenKitchen.app.enums.Role
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "User counts summary")
data class UserCountsResponse(
    @param:Schema(description = "Total users", example = "1234")
    val total: Long,
    @param:Schema(description = "Counts by role")
    val byRole: Map<Role, Long>
)

