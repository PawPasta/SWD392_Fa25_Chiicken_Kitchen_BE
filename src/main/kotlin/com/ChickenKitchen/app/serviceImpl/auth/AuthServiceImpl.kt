package com.ChickenKitchen.app.serviceImpl.auth

import com.ChickenKitchen.app.enum.Role
import com.ChickenKitchen.app.handler.AuthenticationException
import com.ChickenKitchen.app.model.dto.request.FirebaseLoginRequest
import com.ChickenKitchen.app.model.dto.request.TokenRefreshRequest
import com.ChickenKitchen.app.model.dto.response.TokenResponse
import com.ChickenKitchen.app.model.entity.auth.UserSession
import com.ChickenKitchen.app.repository.auth.UserSessionRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.auth.AuthService
import com.ChickenKitchen.app.handler.TokenException
// Removed internal email/password login
import com.ChickenKitchen.app.model.entity.user.User
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


    override fun loginWithFirebase(req: FirebaseLoginRequest): TokenResponse {
        val payload = jwtService.decodeUnverifiedJwtPayload(req.idToken)

        val email = payload.path("email").asText(null)
            ?: throw AuthenticationException("Invalid Firebase token: email is missing")
        val fullName = payload.path("name").asText(email)
        val picture = payload.path("picture").asText(null)
        val provider = payload.path("firebase").path("sign_in_provider").asText("firebase")
        val uid = payload.path("user_id").asText(payload.path("sub").asText(null))

        val user = userRepository.findByEmail(email)
            ?: userRepository.save(
                User(
                    fullName = fullName, 
                    email = email,
                    role = Role.USER,
                    isActive = true,
                    isVerified = payload.path("email_verified").asBoolean(true),
                    imageURL = picture,
                    provider = provider,
                ).also { it.uid = uid }
            )

        if (!user.isActive) throw AuthenticationException("Your account is locked!")

        cancelAllPreviousSessions(user)

        val accessToken = jwtService.generateUserToken(user.email, user.role.name)
        val refreshToken = jwtService.generateRefreshToken(user.email)
        val expiryAt = jwtService.getExpiryDate(false)

        userSessionRepository.save(
            UserSession(
                user = user,
                sessionToken = accessToken,
                refreshToken = refreshToken,
                expiresAt = Timestamp(expiryAt.time),
                lastActivity = Timestamp(System.currentTimeMillis())
            )
        )

        return TokenResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    // Decode helpers moved to JwtServiceImpl

    override fun refreshToken(req: TokenRefreshRequest): TokenResponse {
        val userSession = userSessionRepository.findByRefreshTokenAndIsCancelledFalse(req.refreshToken)
            ?: throw TokenException("Invalid refresh token session")

        if (userSession.expiresAt?.before(Timestamp(System.currentTimeMillis())) ?: true) {
            userSession.isCancelled = true
            userSessionRepository.save(userSession)
            throw TokenException("Refresh token session has expired. Please login again.")
        }

        val user = userSession.user

        cancelAllPreviousSessions(user)

        val newAccessToken = jwtService.generateUserToken(user.email, user.role.name)
        val newRefreshToken = jwtService.generateRefreshToken(user.email)
        val newExpiryAt = jwtService.getExpiryDate(false)

        userSessionRepository.save(
            UserSession(
                user = user,
                sessionToken = newAccessToken,
                refreshToken = newRefreshToken,
                expiresAt = Timestamp(newExpiryAt.time),
                lastActivity = Timestamp(System.currentTimeMillis()),
            )
        )

        return TokenResponse(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }

    override fun logout() {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        val sessions = userSessionRepository.findAllByUserEmailAndIsCancelledFalse(auth.name)

        if (sessions.isEmpty()) throw TokenException("No active session found for user: ${auth.name}")
        if (sessions.size > 1) throw TokenException("Multiple active sessions found for user: ${auth.name}")

        sessions[0].isCancelled = true
        userSessionRepository.saveAll(sessions)
        SecurityContextHolder.clearContext()
    }

    private fun cancelAllPreviousSessions(user: User) {
        // Cancel all previous sessions (regardless of status) before creating a new session
        user.id?.let { userId ->
            val sessions = userSessionRepository.findAllByUserId(userId)
            if (sessions.isNotEmpty()) {
                sessions.forEach { it.isCancelled = true }
                userSessionRepository.saveAll(sessions)
            }
        }
    }
}
