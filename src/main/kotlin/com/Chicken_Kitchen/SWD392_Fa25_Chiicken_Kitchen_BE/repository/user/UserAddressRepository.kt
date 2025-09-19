package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.user

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.user.UserAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAddressRepository : JpaRepository<UserAddress, Long> {
    fun findAllByUserId(userId: Long): List<UserAddress>
    fun findByUserIdAndIsDefault(userId: Long, isDefault: Boolean = true): UserAddress?
}
