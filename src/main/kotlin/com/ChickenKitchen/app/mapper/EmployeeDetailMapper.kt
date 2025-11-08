package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.dto.response.EmployeeDetailFullResponse
import com.ChickenKitchen.app.model.entity.user.EmployeeDetail

fun EmployeeDetail.toEmployeeDetailFullResponse(): EmployeeDetailFullResponse =
    EmployeeDetailFullResponse(
        id = this.id!!,
        position = this.position,
        isActive = this.isActive,
        note = this.note,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        store = this.store.toStoreResponse(),
        user = this.user.toUserResponse(),
    )

