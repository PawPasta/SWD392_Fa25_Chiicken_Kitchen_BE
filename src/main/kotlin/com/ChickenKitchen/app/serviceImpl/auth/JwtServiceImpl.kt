package com.ChickenKitchen.app.serviceImpl.auth    

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import java.util.Date
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.service.auth.JwtService
import com.ChickenKitchen.app.handler.TokenException
import io.jsonwebtoken.security.SignatureException 
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.Base64

@Component
class JwtServiceImpl(
    @param:Value("\${jwt.secret}") private val secret: String,
    @param:Value("\${jwt.expiration}") private val expiration: Long,
    private val userRepository: UserRepository
): JwtService {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    override fun generateUserToken(email: String, role: String): String =
        Jwts.builder()
            .setSubject(email)
            .claim("role", role)
            .setIssuedAt(Date())
            .setExpiration(getExpiryDate(true))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

    override fun generateRefreshToken(email: String): String =
        Jwts.builder()
            .setSubject(email)
            .claim("role", "REFRESH")
            .setIssuedAt(Date())
            .setExpiration(getExpiryDate(false))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

    override fun getExpiryDate(accessToken: Boolean): Date {
        val now = Date()
        return if (accessToken) Date(now.time + expiration) else Date(now.time + 10 * expiration)
    }

    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
            val expiration = claims.expiration
            userRepository.existsByEmail(userDetails.username) && expiration.after(Date())
        } catch (e: ExpiredJwtException) {
            throw TokenException("Token has expired")
        } catch (e: MalformedJwtException) {
            throw TokenException("Invalid token format")
        } catch (e: SignatureException) {
            throw TokenException("Invalid token signature")
        } catch (e: Exception) {
            throw TokenException("Token validation failed: ${e.message}")
        }
    }

    override fun getEmail(token: String): String? =
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject
        } catch (e: Exception) {
            throw TokenException("Failed to extract email from token: ${e.message}")
        }

    override fun getRole(token: String): String? =
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body["role"] as String
        } catch (e: Exception) {
            throw TokenException("Failed to extract role from token: ${e.message}")
        }

    override fun getAudience(token: String): String? =
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.audience
        } catch (e: Exception) {
            throw TokenException("Failed to extract audience from token: ${e.message}")
        }

    override fun decodeUnverifiedJwtPayload(token: String): JsonNode {
        try {
            val parts = token.split(".")
            if (parts.size < 2) throw TokenException("Invalid token structure")
            val decoder = Base64.getUrlDecoder()
            val payloadBytes = decoder.decode(padBase64(parts[1]))
            val json = String(payloadBytes, Charsets.UTF_8)
            val mapper = ObjectMapper()
            return mapper.readTree(json)
        } catch (e: Exception) {
            throw TokenException("Failed to decode token payload: ${e.message}")
        }
    }

    private fun padBase64(s: String): String {
        val rem = s.length % 4
        return if (rem == 0) s else s + "=".repeat(4 - rem)
    }
}
