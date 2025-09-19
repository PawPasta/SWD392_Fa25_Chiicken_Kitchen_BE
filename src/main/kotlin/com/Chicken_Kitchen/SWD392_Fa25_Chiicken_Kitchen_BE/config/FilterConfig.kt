// package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.config

// import jakarta.servlet.FilterChain
// import jakarta.servlet.http.HttpServletRequest
// import jakarta.servlet.http.HttpServletResponse
// import org.springframework.security.core.context.SecurityContextHolder
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
// import org.springframework.security.core.userdetails.UserDetailsService
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
// import org.springframework.stereotype.Component
// import org.springframework.web.filter.OncePerRequestFilter
// import org.springframework.web.servlet.HandlerExceptionResolver
// import com.example.demo.service.auth.JwtService
// import com.example.demo.exception.TokenException

// @Component
// class FilterConfig(
//     private val jwtService: JwtService,
//     private val userDetailsService: UserDetailsService,
//     private val handlerExceptionResolver: HandlerExceptionResolver
// ) : OncePerRequestFilter() {

//     private val publicApis = listOf(
//         "/v3/api-docs",
//         "/swagger-ui",
//         "/swagger-ui.html",
//         "/api/auth/register",
//         "/api/auth/login",
//         "/api/auth/verify",
//         "/api/auth/forgot-password",
//         "/api/auth/reset-password"
//     )

//     override fun doFilterInternal(
//         request: HttpServletRequest,
//         response: HttpServletResponse,
//         filterChain: FilterChain
//     ) {
//         try {
//             val requestUri = request.requestURI
//             val isPublic = publicApis.any { requestUri.startsWith(it) }
//             val authHeader = request.getHeader("Authorization")

//             // Nếu là private API mà không có token
//             if (!isPublic && (authHeader == null || !authHeader.startsWith("Bearer "))) {
//                 throw TokenException("Missing or invalid Authorization header")
//             }

//             // Nếu có token thì parse
//             if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                 val token = authHeader.substring(7)
//                 val username = jwtService.getUsername(token)

//                 if (username != null && SecurityContextHolder.getContext().authentication == null) {
//                     val userDetails = userDetailsService.loadUserByUsername(username)

//                     if (jwtService.isTokenValid(token, userDetails)) {
//                         val authToken = UsernamePasswordAuthenticationToken(
//                             userDetails,
//                             null,
//                             userDetails.authorities
//                         )
//                         authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
//                         SecurityContextHolder.getContext().authentication = authToken
//                     } else {
//                         throw TokenException("Invalid or expired token")
//                     }
//                 }
//             }

//             filterChain.doFilter(request, response)
//         } catch (ex: Exception) {
//             handlerExceptionResolver.resolveException(request, response, null, ex)
//         }
//     }
// }
