package com.ChickenKitchen.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()

        config.allowedOrigins = listOf(
            "http://localhost:3000",
            "http://localhost:5173",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:5173"
        )
        config.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type", "X-Requested-With")
        config.exposedHeaders = listOf("Authorization", "Content-Disposition")
        config.allowCredentials = true
        config.maxAge = 3600

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}

