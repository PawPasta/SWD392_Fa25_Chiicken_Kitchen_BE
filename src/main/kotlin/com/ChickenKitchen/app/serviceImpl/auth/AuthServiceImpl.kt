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
import com.ChickenKitchen.app.model.dto.request.LoginRequest
import com.ChickenKitchen.app.model.entity.user.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val userSessionRepository: UserSessionRepository,
    private val jwtService: JwtServiceImpl,
    private val passwordEncoder: PasswordEncoder
) : AuthService {


    override fun loginWithFirebase(req: FirebaseLoginRequest): TokenResponse {
        val email = req.email
        val fullName = req.displayName ?: req.email

        val user = userRepository.findByEmail(req.email)
            ?: userRepository.save(
                User(
                    fullName = fullName,
                    email = email,
                    role = Role.USER,
                    isActive = true,
                    isVerified = true,
                    imageURL = req.photoURL,
                    password = "haha",
                    provider = req.providerId
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
                expiresAt = Timestamp(expiryAt.time),
                lastActivity = Timestamp(System.currentTimeMillis())
            )
        )

        return TokenResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    override fun login(req: LoginRequest): TokenResponse {
        val user = userRepository.findByEmail(req.email)
            ?: throw AuthenticationException("Cannot find employee")

        val encoder = BCryptPasswordEncoder()

        if (!encoder.matches(req.password, user.password)) {
            throw AuthenticationException("Invalid credentials")
        }

        if (!user.isActive) throw AuthenticationException("Your account is locked!")

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

    override fun refreshToken(req: TokenRefreshRequest): TokenResponse {
        val userSession = userSessionRepository.findByRefreshTokenAndIsCancelledFalse(req.refreshToken)
            ?: throw TokenException("Invalid refresh token session")

        if (userSession.expiresAt?.before(Timestamp(System.currentTimeMillis())) ?: true) {
            userSession.isCancelled = true
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
}
