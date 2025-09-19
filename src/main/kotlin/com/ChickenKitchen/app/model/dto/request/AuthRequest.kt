package com.ChickenKitchen.app.model.dto.request

data class RegisterRequest (
    val username: String,
    val email: String,
    val password: String,
)

data class LoginRequest (
    val username: String,
    val password: String,
)

data class ForgotPasswordRequest (
    val email: String
)

data class ChangePasswordRequest (
    val oldPassword: String,
    val newPassword: String
)

data class ResetPasswordRequest (
    val token: String,
    val newPassword: String
)

data class TokenRefreshRequest (
    val refreshToken: String
)