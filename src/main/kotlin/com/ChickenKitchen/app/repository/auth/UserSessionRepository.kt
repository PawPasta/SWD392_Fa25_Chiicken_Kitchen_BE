package com.ChickenKitchen.app.repository.auth

import com.ChickenKitchen.app.model.entity.auth.UserSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSessionRepository : JpaRepository<UserSession, Long> {
    fun findBySessionToken(sessionToken: String): UserSession?
    fun findByRefreshToken(refreshToken: String): UserSession?
    fun findByRefreshTokenAndIsCancelledFalse(refreshToken: String): UserSession?
    fun findAllByUserEmailAndIsCancelledFalse(email: String): List<UserSession>
    fun findAllByUserId(userId: Long): List<UserSession>
    fun deleteBySessionToken(sessionToken: String)
}
