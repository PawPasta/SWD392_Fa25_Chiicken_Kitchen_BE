package com.ChickenKitchen.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.authentication.AuthenticationManager
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.http.HttpMethod

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
class SecurityConfig( 
    private val filterConfig: FilterConfig
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .authorizeHttpRequests {
                // Public docs
                it.requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                ).permitAll()

                // Temporarily public application APIs
                it.requestMatchers(
                    "/api/auth/**",
                    "/api/categories/**",
                    "/api/daily-menu/**",
                    "/api/ingredient/**",
                    "/api/menu-items/**",
                    "/api/nutrients/**",
                    // "/api/orders/**",
                    "/api/promotion/**",
                    "/api/steps/**",
                    "/api/store/**",
                    "/api/transaction/**",
                    "/api/users/**",
                    "/api/notifications/**",
                    "/api/payments/momo/test"
                ).permitAll()

                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // .anyRequest().authenticated() // Temporarily disabled to make everything public
                .anyRequest().permitAll()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(filterConfig, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint { _, response, authException ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
                }
            }
            .formLogin { it.disable() } 
            .httpBasic { it.disable() } 
            

        return http.build()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}
