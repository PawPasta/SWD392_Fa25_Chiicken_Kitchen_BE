package com.ChickenKitchen.app.repository.user

import com.ChickenKitchen.app.model.entity.user.UserAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAddressRepository : JpaRepository<UserAddress, Long> {
    fun findAllByUserId(userId: Long): List<UserAddress>
    fun findAllByUserUsernameAndId(userUsername: String, id: Long): List<UserAddress>
    fun findAllByUserUsernameAndIsDefaultTrue(userUsername: String): List<UserAddress>
    fun findByUserIdAndIsDefault(userId: Long, isDefault: Boolean = true): UserAddress?
}
