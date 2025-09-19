package com.Chicken_Kitchen.repository.user

import com.Chicken_Kitchen.model.entity.user.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WalletRepository : JpaRepository<Wallet, Long> {
    fun findByUserId(userId: Long): Optional<Wallet>
}
