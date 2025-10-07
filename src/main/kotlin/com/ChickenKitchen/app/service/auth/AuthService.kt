package com.ChickenKitchen.app.service.auth

import com.ChickenKitchen.app.model.dto.request.FirebaseLoginRequest
import com.ChickenKitchen.app.model.dto.request.TokenRefreshRequest
import com.ChickenKitchen.app.model.dto.response.TokenResponse

interface AuthService {
    fun login(req: FirebaseLoginRequest): TokenResponse
    fun refreshToken(req: TokenRefreshRequest): TokenResponse
    fun logout()
}
