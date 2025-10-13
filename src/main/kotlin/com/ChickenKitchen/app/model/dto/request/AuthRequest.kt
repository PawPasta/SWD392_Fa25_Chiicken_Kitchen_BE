package com.ChickenKitchen.app.model.dto.request

// Firebase login: backend only receives the Firebase ID token
data class FirebaseLoginRequest(
    val accessToken: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class TokenRefreshRequest(
    val refreshToken: String
)
