package com.ChickenKitchen.app.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.http.ResponseEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import com.ChickenKitchen.app.service.auth.AuthService
import com.ChickenKitchen.app.model.dto.request.RegisterRequest
import com.ChickenKitchen.app.model.dto.request.LoginRequest
import com.ChickenKitchen.app.model.dto.request.ChangePasswordRequest
import com.ChickenKitchen.app.model.dto.request.ForgotPasswordRequest
import com.ChickenKitchen.app.model.dto.request.ResetPasswordRequest
import com.ChickenKitchen.app.model.dto.request.TokenRefreshRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.model.dto.response.TokenResponse

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @Operation(summary = "Register new user")
    @ApiResponse(responseCode = "200", description = "Registered successfully")
    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): ResponseEntity<ResponseModel> {
        authService.register(req)
        return ResponseEntity.ok(ResponseModel.success(null, "Register successfully!"))
    }

    @Operation(summary = "Login user account")
    @ApiResponse(responseCode = "200", description = "Login successfully!")
    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<ResponseModel> {
        val tokenResponse = authService.login(req)
        return ResponseEntity.ok(ResponseModel.success(tokenResponse, "Login successfully!"))
    }

    @Operation(summary = "Verify email")
    @GetMapping("/verify")
    fun verifyEmail(@RequestParam token: String): ResponseEntity<ResponseModel> {
        authService.verifyEmail(token);
        return ResponseEntity.ok(ResponseModel.success(null, "Email verified successfully!"))
    }

    @Operation(summary = "Chage password when user is logged in")
    @PutMapping("/change-password")
    fun changePassword(@RequestBody req: ChangePasswordRequest): ResponseEntity<ResponseModel> {
        authService.changePassword(req)
        return ResponseEntity.ok(ResponseModel.success(null, "Password updated successfully"))
    }

    @Operation(summary = "Send email to reset password")
    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody req: ForgotPasswordRequest): ResponseEntity<ResponseModel> {
        authService.forgotPassword(req)
        return ResponseEntity.ok(ResponseModel.success(null, "Email send successfully"))
    }

    @Operation(summary = "Reset password using token sent to email")
    @PutMapping("/reset-password")
    fun resetPassword(@RequestBody req: ResetPasswordRequest): ResponseEntity<ResponseModel> {
        authService.resetPassword(req)
        return ResponseEntity.ok(ResponseModel.success(null, "Password updated successfully"))
    }

    @Operation(summary = "Refresh JWT token")
    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody req: TokenRefreshRequest): ResponseEntity<ResponseModel> {
        val newToken = authService.refreshToken(req)
        return ResponseEntity.ok(ResponseModel.success(newToken, "Token refreshed successfully"))
    }
}
