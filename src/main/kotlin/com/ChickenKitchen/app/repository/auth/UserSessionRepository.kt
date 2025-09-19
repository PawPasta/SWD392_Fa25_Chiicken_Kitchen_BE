package com.ChickenKitchen.app.repository.auth

import com.ChickenKitchen.app.model.entity.auth.UserSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSessionRepository : JpaRepository<UserSession, Long> {
    fun findBySessionToken(sessionToken: String): UserSession?
    fun findByRefreshToken(refreshToken: String): UserSession?
    fun findByRefreshTokenAndIsCanceledFalse(refreshToken: String): UserSession?
    fun findAllByUserUsernameAndIsCanceledFalse(username: String): List<UserSession>
    fun findAllByUserId(userId: Long): List<UserSession>
    fun deleteBySessionToken(sessionToken: String)
}
