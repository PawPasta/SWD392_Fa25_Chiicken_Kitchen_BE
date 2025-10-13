package com.ChickenKitchen.app.mapper
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.model.dto.response.UserResponse
import com.ChickenKitchen.app.model.dto.response.UserDetailResponse
import com.ChickenKitchen.app.model.dto.response.UserProfileResponse

fun User.toUserResponse(): UserResponse =
    UserResponse(
        id = this.id!!,
        fullName = this.fullName,
        email = this.email,
        roles = this.role,
        isActive = this.isActive,
        imageURL = this.imageURL,
    )

fun List<User>.toUserResponseList(): List<UserResponse> =
    this.map { it.toUserResponse() }

fun User.toUserDetailResponse(): UserDetailResponse =
    UserDetailResponse(
        id = this.id!!,
        fullName = this.fullName,
        email = this.email,
        roles = this.role,
        isActive = this.isActive,
        birthday = this.birthday,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),

    )


fun User.toUserProfileResponse(): UserProfileResponse =
    UserProfileResponse(
        fullName = this.fullName,
        email = this.email,
        birthday = this.birthday,
        createdAt = this.createdAt.toString(),
    )
