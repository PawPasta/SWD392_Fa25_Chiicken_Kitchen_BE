package com.ChickenKitchen.app.service.auth

import com.ChickenKitchen.app.model.dto.request.FirebaseLoginRequest
import com.ChickenKitchen.app.model.dto.request.LoginRequest
import com.ChickenKitchen.app.model.dto.request.TokenRefreshRequest
import com.ChickenKitchen.app.model.dto.response.TokenResponse

interface AuthService {
    fun loginWithFirebase(req: FirebaseLoginRequest): TokenResponse
    fun login(req: LoginRequest): TokenResponse
    fun refreshToken(req: TokenRefreshRequest): TokenResponse
    fun logout()
}
