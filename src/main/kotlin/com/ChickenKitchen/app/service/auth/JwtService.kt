package com.ChickenKitchen.app.service.auth

import java.util.Date

interface JwtService {
    fun generateUserToken(email: String, role: String): String
    fun generateRefreshToken(email: String): String
    fun getExpiryDate(accessToken: Boolean): Date
    fun isTokenValid(token: String, userDetails: org.springframework.security.core.userdetails.UserDetails): Boolean
    fun getEmail(token: String): String?
    fun getRole(token: String): String?
    fun getAudience(token: String): String?
    // Decode a JWT without signature verification (Base64URL decode payload)
    fun decodeUnverifiedJwtPayload(token: String): com.fasterxml.jackson.databind.JsonNode
}
