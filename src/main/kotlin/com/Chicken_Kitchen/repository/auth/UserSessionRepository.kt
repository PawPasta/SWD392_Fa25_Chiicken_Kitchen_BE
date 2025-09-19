package com.Chicken_Kitchen.repository.auth

import com.Chicken_Kitchen.model.entity.user.UserSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSessionRepository : JpaRepository<UserSession, Long> {
    fun findBySessionToken(sessionToken: String): Optional<UserSession>
    fun findByRefreshToken(refreshToken: String): Optional<UserSession>
    fun findAllByUserId(userId: Long): List<UserSession>
    fun deleteBySessionToken(sessionToken: String)
}
