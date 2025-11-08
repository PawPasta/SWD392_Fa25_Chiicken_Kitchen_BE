package com.ChickenKitchen.app.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import com.ChickenKitchen.app.service.auth.JwtService
import com.ChickenKitchen.app.handler.TokenException

@Component
class FilterConfig(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val handlerExceptionResolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    private val publicApis = listOf(
        // Swagger/OpenAPI


        "/v3/api-docs",
        "/swagger-ui",
        "/swagger-ui.html",

        // Auth endpoints
        "/api/auth/login",
        "/api/auth/refresh",
        "/api/auth/logout",

        // Temporarily making all application APIs public
        // Add base paths for each controller
        "/api/categories",
        "/api/ingredient",
        "/api/menu-items",
        "/api/nutrients",
        // "/api/orders",
        "/api/promotion",
        "/api/steps",
        "/api/store",
        "/api/transaction",
        "/api/users",

        "/api/notifications",
        "/api/payments/momo/test",

        "/api/dishes",
        "/api/dashboard"

        // Broad allow for all APIs (temporary)
        // "/api" // uncomment to allow absolutely all under /api
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            if (request.method.equals("OPTIONS", ignoreCase = true)) {
                filterChain.doFilter(request, response)
                return
            }
            val requestUri = request.requestURI

            // Allow public APIs except profile endpoint which must be authenticated
            val isProfileEndpoint = requestUri.startsWith("/api/users/me")
            if (!isProfileEndpoint && publicApis.any { requestUri.startsWith(it) }) {
                filterChain.doFilter(request, response)
                return
            }

           val authHeader = request.getHeader("Authorization")

           // Chặn lại kiểm tra giấy tờ
           if (authHeader == null || !authHeader.startsWith("Bearer ")) throw TokenException("Missing or invalid Authorization header")

           // Có giấy thì vô
           val token = authHeader.substring(7)
           val email = jwtService.getEmail(token)

           if (email != null && SecurityContextHolder.getContext().authentication == null) {
               val userDetails = userDetailsService.loadUserByUsername(email)

               if (jwtService.isTokenValid(token, userDetails)) {
                   val authToken = UsernamePasswordAuthenticationToken(
                       userDetails,
                       null,
                       userDetails.authorities
                   )
                   authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                   SecurityContextHolder.getContext().authentication = authToken
               } else {
                   throw TokenException("Invalid or expired token")
               }
           }

            // Do not override existing authentication; keep the validated user context

            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            handlerExceptionResolver.resolveException(request, response, null, ex)
        }
    }
}
