package com.ChickenKitchen.app.serviceImpl.auth

import com.ChickenKitchen.app.model.dto.request.FirebaseLoginRequest
import com.ChickenKitchen.app.model.dto.request.TokenRefreshRequest
import com.ChickenKitchen.app.model.dto.response.TokenResponse
import com.ChickenKitchen.app.model.entity.auth.UserSession
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.repository.auth.UserSessionRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.auth.AuthService
import com.ChickenKitchen.app.handler.AuthenticationException
import com.ChickenKitchen.app.handler.TokenException
import com.ChickenKitchen.app.handler.UserNotFoundException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.sql.Timestamp
 

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val userSessionRepository: UserSessionRepository,
    private val jwtService: JwtServiceImpl,
) : AuthService {

    override fun login(req: FirebaseLoginRequest): TokenResponse {
        val email = req.email
        val fullName = req.name ?: email

        val user = userRepository.findByEmail(email)
            ?: userRepository.save(
                User(
                    fullname = fullName,
                    email = email,
                )
            )

        if (!user.isActive) throw AuthenticationException("Your account is locked!")

        val accessToken = jwtService.generateUserToken(user.email, user.role.name)
        val refreshToken = jwtService.generateRefreshToken(user.email)
        val expiryAt = jwtService.getExpiryDate(false)

        userSessionRepository.save(
            UserSession(
                user = user,
                sessionToken = accessToken,
                refreshToken = refreshToken,
                expiredAt = Timestamp(expiryAt.time),
                lastActivity = Timestamp(System.currentTimeMillis()),
            )
        )

        return TokenResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    override fun refreshToken(req: TokenRefreshRequest): TokenResponse {
        val userSession = userSessionRepository.findByRefreshTokenAndIsCanceledFalse(req.refreshToken)
            ?: throw TokenException("Invalid refresh token session")

        if (userSession.expiredAt.before(Timestamp(System.currentTimeMillis()))) {
            userSession.isCanceled = true
            userSessionRepository.save(userSession)
            throw TokenException("Refresh token session has expired. Please login again.")
        }

        val user = userSession.user
        val newAccessToken = jwtService.generateUserToken(user.email, user.role.name)
        val newRefreshToken = jwtService.generateRefreshToken(user.email)
        val newExpiryAt = jwtService.getExpiryDate(false)

        userSessionRepository.save(
            UserSession(
                user = user,
                sessionToken = newAccessToken,
                refreshToken = newRefreshToken,
                expiredAt = Timestamp(newExpiryAt.time),
                lastActivity = Timestamp(System.currentTimeMillis()),
            )
        )

        return TokenResponse(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }

    override fun logout() {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        val sessions = userSessionRepository.findAllByUserEmailAndIsCanceledFalse(auth.name)

        if (sessions.isEmpty()) throw TokenException("No active session found for user: ${auth.name}")
        if (sessions.size > 1) throw TokenException("Multiple active sessions found for user: ${auth.name}")

        sessions[0].isCanceled = true
        userSessionRepository.saveAll(sessions)
        SecurityContextHolder.clearContext()
    }
}
