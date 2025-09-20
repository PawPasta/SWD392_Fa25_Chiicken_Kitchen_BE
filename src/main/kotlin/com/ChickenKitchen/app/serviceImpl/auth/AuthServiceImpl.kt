package com.ChickenKitchen.app.serviceImpl.auth

import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.beans.factory.annotation.Value
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.model.entity.user.Wallet
import com.ChickenKitchen.app.model.entity.auth.MailToken
import com.ChickenKitchen.app.model.entity.auth.UserSession
import com.ChickenKitchen.app.model.dto.request.RegisterRequest
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.repository.user.WalletRepository
import com.ChickenKitchen.app.repository.auth.UserSessionRepository
import com.ChickenKitchen.app.model.dto.request.LoginRequest
import com.ChickenKitchen.app.model.dto.response.TokenResponse
import com.ChickenKitchen.app.model.dto.request.ChangePasswordRequest
import com.ChickenKitchen.app.model.dto.request.ForgotPasswordRequest
import com.ChickenKitchen.app.model.dto.request.ResetPasswordRequest
import com.ChickenKitchen.app.model.dto.request.TokenRefreshRequest
import com.ChickenKitchen.app.repository.auth.MailTokenRepository
import com.ChickenKitchen.app.util.EmailUtil
import com.ChickenKitchen.app.service.auth.AuthService
import com.ChickenKitchen.app.handler.UserAlreadyExistsException
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.handler.AuthenticationException
import com.ChickenKitchen.app.handler.TokenException
import com.ChickenKitchen.app.handler.EmailSendException
import com.ChickenKitchen.app.handler.AccessDeniedException
import com.ChickenKitchen.app.enum.MailType

import java.util.UUID
import java.util.Date
import java.sql.Timestamp
import java.math.BigDecimal

@Service
class AuthServiceImpl (
    private val userRepository: UserRepository,
    private val mailTokenRepository: MailTokenRepository,
    private val userSessionRepository: UserSessionRepository,
    private val walletRepository: WalletRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailUtil: EmailUtil,
    private val jwtService: JwtServiceImpl,
    @Value("\${mail.token.expiration}") private val expiration: Long,
): AuthService {

    override fun register(req: RegisterRequest) {
        if (userRepository.existsByUsername(req.username)) {
            throw UserAlreadyExistsException("Username already exists")
        }
        if (userRepository.existsByEmail(req.email)) {
            throw UserAlreadyExistsException("Email already exists")
        }

        val user = userRepository.save(
            User(username = req.username, email = req.email, password = passwordEncoder.encode(req.password))
        )

        // Khi đăng kí thành cũng tạo luôn ví nhé!
        walletRepository.save(Wallet(user = user, balance = BigDecimal("0.00")))

        val token = UUID.randomUUID().toString()
        val expiredAt = Timestamp(System.currentTimeMillis() + expiration)

        mailTokenRepository.save(MailToken(user = user, token = token, type = MailType.VERIFY_EMAIL, expiredAt = expiredAt))

        try {
            emailUtil.
            send(
                user.email,
                "Please verify your email",
                """
                Hi,
                
                Please verify your email by clicking the link below:
                http://localhost:8080/api/verify?token=$token
                
                This link will expire in 24 hours.
                """.trimIndent()
            )
        } catch (e: Exception) {
            throw EmailSendException("Failed to send verification email", e)
        }
    }

    override fun login(req: LoginRequest): TokenResponse { // Còn thiếu device info đợi viết sau
        val user = userRepository.findByUsername(req.username) 
        ?: throw AuthenticationException("Invalid username")

        if (!user.isVerify) throw AuthenticationException("Please verify your email first!")
        if (!passwordEncoder.matches(req.password, user.password)) throw AuthenticationException("Invalid username or password")
        if (!user.isActive) throw AccessDeniedException("Your account is locked!")

        val accessToken = jwtService.generateUserToken(user.username, user.role.name)
        val refreshToken = jwtService.generateRefreshToken(user.username)
        val expiredAt = jwtService.getExpiryDate(false)

        userSessionRepository.save(UserSession(
            user = user, sessionToken = accessToken, refreshToken = refreshToken, 
            expiredAt = Timestamp(expiredAt.time), lastActivity = Timestamp(System.currentTimeMillis())))

        return TokenResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    override fun verifyEmail(token: String) {

        val mailToken = mailTokenRepository.findByToken(token)
            ?: throw TokenException("Invalid verification token")

        if (mailToken.expiredAt.before(Timestamp(System.currentTimeMillis()))) throw TokenException("Verification token has expired")

        mailToken.user.isVerify = true
        userRepository.save(mailToken.user)
    }

    override fun changePassword(req: ChangePasswordRequest) {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException("User not found")

        if (req.oldPassword == req.newPassword) throw AuthenticationException("New password must be different from old password")
        if (!passwordEncoder.matches(req.oldPassword, user.password)) throw AuthenticationException("Old password is incorrect")

        user.password = passwordEncoder.encode(req.newPassword)
        userRepository.save(user)
    }

    override fun forgotPassword(req: ForgotPasswordRequest) {
        val user = userRepository.findByEmail(req.email) ?: throw UserNotFoundException("User not found with email: ${req.email}")

        val currentMail = mailTokenRepository.findAllByUserIdAndTypeAndIsCanceledFalse(userId = user.id!!, type = MailType.RESET_PASSWORD)
        if (currentMail.isNotEmpty()) {
            currentMail.forEach { it.isCanceled = true }
            mailTokenRepository.saveAll(currentMail)
        }

        val token = UUID.randomUUID().toString()
        val expiredAt = Timestamp(System.currentTimeMillis() + expiration)

        mailTokenRepository.save(MailToken(user = user, token = token, type = MailType.RESET_PASSWORD, expiredAt = expiredAt))

        try {
            emailUtil.
            send(
                user.email,
                "Reset your password",
                """
                Hi,
                
                You requested to reset your password.
                Please click the link below to reset it:
                http://localhost:8080/api/reset-password?token=$token
                
                This link will expire in 24 hours.
                """.trimIndent()
            )
        } catch (e: Exception) {
            throw EmailSendException("Failed to send reset password email", e)
        }
    }

    override fun resetPassword(req: ResetPasswordRequest) {

        val mailToken = mailTokenRepository.findAllByTokenAndTypeAndIsCanceledFalse(req.token, type = MailType.RESET_PASSWORD)
        if (mailToken.isEmpty()) throw TokenException("Invalid or expired reset password token")
        if (mailToken.size > 1) throw TokenException("Multiple valid reset password tokens found. Please request a new one.")
        if (mailToken[0].expiredAt.before(Timestamp(System.currentTimeMillis()))) throw TokenException("Reset password token has expired")
        if (mailToken[0].isCanceled) throw TokenException("Reset password token has been canceled")

        mailToken[0].user.password = passwordEncoder.encode(req.newPassword)
        userRepository.save(mailToken[0].user)
        mailToken[0].isCanceled = true
        mailTokenRepository.save(mailToken[0])
    }

    override fun refreshToken(req: TokenRefreshRequest): TokenResponse {

        val userSession = userSessionRepository.findByRefreshTokenAndIsCanceledFalse(req.refreshToken) ?: throw TokenException("Invalid refresh token session")

        if (userSession.expiredAt.before(Timestamp(System.currentTimeMillis()))) {
            userSession.isCanceled = true
            userSessionRepository.save(userSession)
            throw TokenException("Refresh token session has expired. Please login again.")
        }

        val user = userSession.user

        val newAccessToken = jwtService.generateUserToken(user.username, user.role.name)
        val newRefreshToken = jwtService.generateRefreshToken(user.username)
        val newExpiryAt = jwtService.getExpiryDate(false)

        userSessionRepository.save(UserSession(
            user = user,
            sessionToken = newAccessToken,
            refreshToken = newRefreshToken,
            expiredAt = Timestamp(newExpiryAt.time),
            lastActivity = Timestamp(System.currentTimeMillis()),
        ))

        return TokenResponse(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }

    override fun logout() {
        val auth: Authentication = SecurityContextHolder.getContext().authentication

        val userSession = userSessionRepository.findAllByUserUsernameAndIsCanceledFalse(auth.name)

        if (userSession.isEmpty()) throw TokenException("No active session found for user: ${auth.name}")
        if (userSession.size > 1) throw TokenException("Multiple active sessions found for user: ${auth.name}")

        userSession[0].isCanceled = true
        
        userSessionRepository.saveAll(userSession)
        
        SecurityContextHolder.clearContext()
    }
}
