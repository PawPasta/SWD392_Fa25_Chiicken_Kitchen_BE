package com.ChickenKitchen.app.model.dto.request

// Firebase login: backend only receives the Firebase ID token
data class FirebaseLoginRequest(
    val idToken: String,
    val fcmToken: String? = null,
)

// Removed internal email/password login request

data class TokenRefreshRequest(
    val refreshToken: String
)
