package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.service.auth

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.dto.request.RegisterRequest
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.dto.request.ChangePasswordRequest
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.dto.request.ForgotPasswordRequest
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.dto.request.LoginRequest
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.dto.request.ResetPasswordRequest
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.dto.request.TokenRefreshRequest
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.dto.response.TokenResponse

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