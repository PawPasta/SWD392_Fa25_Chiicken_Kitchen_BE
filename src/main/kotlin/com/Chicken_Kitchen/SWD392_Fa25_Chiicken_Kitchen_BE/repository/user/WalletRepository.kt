package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.user

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.user.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WalletRepository : JpaRepository<Wallet, Long> {
    fun findByUserId(userId: Long): Optional<Wallet>
}
