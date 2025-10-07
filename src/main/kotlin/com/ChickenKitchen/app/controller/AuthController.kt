package com.ChickenKitchen.app.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.http.ResponseEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import com.ChickenKitchen.app.service.auth.AuthService
import com.ChickenKitchen.app.model.dto.request.FirebaseLoginRequest
import com.ChickenKitchen.app.model.dto.request.TokenRefreshRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @Operation(summary = "Login via Firebase payload (test.json)")
    @ApiResponse(responseCode = "200", description = "Login successfully!")
    @PostMapping("/login")
    fun login(@RequestBody req: FirebaseLoginRequest): ResponseEntity<ResponseModel> {
        val tokenResponse = authService.login(req)
        return ResponseEntity.ok(ResponseModel.success(tokenResponse, "Login successfully!"))
    }

    @Operation(summary = "Refresh JWT token")
    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody req: TokenRefreshRequest): ResponseEntity<ResponseModel> {
        val newToken = authService.refreshToken(req)
        return ResponseEntity.ok(ResponseModel.success(newToken, "Token refreshed successfully"))
    }

    @Operation(summary = "Logout user")
    @PostMapping("/logout")
    fun logout(): ResponseEntity<ResponseModel> {
        authService.logout()
        return ResponseEntity.ok(ResponseModel.success(null, "Logout successfully"))
    }
}
