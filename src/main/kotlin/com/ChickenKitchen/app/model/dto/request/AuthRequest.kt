package com.ChickenKitchen.app.model.dto.request

// Firebase-supported login: backend receives user info payload (e.g., from test.json)
data class FirebaseLoginRequest(
    val providerId : String, // e.g., "google.com"
    val uid: String,
    val email: String,
    val displayName: String? = null,
    val photoURL: String? = null,
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class TokenRefreshRequest(
    val refreshToken: String
)
