package com.ChickenKitchen.app.repository.user

import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.model.entity.user.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface WalletRepository : JpaRepository<Wallet, Long> {
    fun findByUser(user: User): Wallet?
}