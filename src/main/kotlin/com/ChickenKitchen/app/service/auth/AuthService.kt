package com.ChickenKitchen.app.service.auth

import com.ChickenKitchen.app.model.dto.request.RegisterRequest
import com.ChickenKitchen.app.model.dto.request.ChangePasswordRequest
import com.ChickenKitchen.app.model.dto.request.ForgotPasswordRequest
import com.ChickenKitchen.app.model.dto.request.LoginRequest
import com.ChickenKitchen.app.model.dto.request.ResetPasswordRequest
import com.ChickenKitchen.app.model.dto.request.TokenRefreshRequest
import com.ChickenKitchen.app.model.dto.response.TokenResponse

interface AuthService {
    fun register(req: RegisterRequest) 
    fun login(req: LoginRequest): TokenResponse
    fun refreshToken(req: TokenRefreshRequest): TokenResponse
    fun logout() 
    fun verifyEmail(token: String)
    fun changePassword(req: ChangePasswordRequest)
    fun forgotPassword(req: ForgotPasswordRequest)
    fun resetPassword(req: ResetPasswordRequest)
}