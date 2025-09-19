package com.ChickenKitchen.app.service.auth

import java.util.Date

interface JwtService {
    fun generateUserToken(username: String, role: String): String
    fun generateRefreshToken(username: String): String
    fun getExpiryDate(accessToken: Boolean): Date
    fun isTokenValid(token: String, userDetails: org.springframework.security.core.userdetails.UserDetails): Boolean
    fun getUsername(token: String): String?
    fun getRole(token: String): String?
    fun getAudience(token: String): String?
}