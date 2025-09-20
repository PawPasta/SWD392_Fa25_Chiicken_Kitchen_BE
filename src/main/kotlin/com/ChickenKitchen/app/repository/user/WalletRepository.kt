package com.ChickenKitchen.app.repository.user

import com.ChickenKitchen.app.model.entity.user.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WalletRepository : JpaRepository<Wallet, Long> {
    fun findByUserId(userId: Long): Wallet?
    fun findByUserUsername(username: String): Wallet?
}
