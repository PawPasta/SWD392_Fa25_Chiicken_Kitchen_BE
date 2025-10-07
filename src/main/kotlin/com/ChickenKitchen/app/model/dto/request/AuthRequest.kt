package com.ChickenKitchen.app.model.dto.request

// Firebase-supported login: backend receives user info payload (e.g., from test.json)
data class FirebaseLoginRequest(
    val uid: String,
    val email: String,
    val name: String? = null,
    val emailVerified: Boolean = false,
    val picture: String? = null,
)

data class TokenRefreshRequest(
    val refreshToken: String
)
