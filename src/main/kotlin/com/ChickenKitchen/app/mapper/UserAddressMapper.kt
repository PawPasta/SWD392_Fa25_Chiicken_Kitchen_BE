package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.user.UserAddress
import com.ChickenKitchen.app.model.dto.response.UserAddressResponse

fun UserAddress.toUserAddressResponse(): UserAddressResponse =
    UserAddressResponse(
        id = this.id!!,
        recipientName = this.recipientName,
        phone = this.phone,
        addressLine = this.addressLine,
        city = this.city,
        isDefault = this.isDefault
    )

fun List<UserAddress>.toUserAddressResponseList(): List<UserAddressResponse> =
    this.map { it.toUserAddressResponse() }