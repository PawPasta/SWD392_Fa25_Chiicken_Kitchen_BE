package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.service.user.UserService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.http.ResponseEntity
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.model.dto.request.CreateUserRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.DeleteMapping
import com.ChickenKitchen.app.model.dto.request.UpdateUserRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserProfileRequest
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    // Chỉ có Admin mới có thể truy cập những endpoint này.

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    @GetMapping("")
    fun getAllUsers(): ResponseEntity<ResponseModel> {
        val users = userService.getAll()
        return ResponseEntity.ok(ResponseModel.success(users, "Get all users successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID (Admin only)")
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val user = userService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(user, "Get user successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new user (Admin only)") 
    @PostMapping("")
    fun createUser(@RequestBody req: CreateUserRequest): ResponseEntity<ResponseModel> {
        val newUser = userService.create(req)
        return ResponseEntity.ok(ResponseModel.success(newUser, "Create user successfully!"))
    } 

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user by ID (Admin only)")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody req: UpdateUserRequest): ResponseEntity<ResponseModel> {
        val updatedUser = userService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(updatedUser, "Update user successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        userService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete user successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change user active status (Admin only)")
    @PatchMapping("/{id}/status")
    fun changeUserStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val updatedUser = userService.changeStatus(id)
        return ResponseEntity.ok(ResponseModel.success(updatedUser, "Change user status successfully!"))
    }

    // Còn những endpoint dưới đây là của tất cả các Role.

    @Operation(summary = "Get profile")
    @GetMapping("/profile")
    fun getProfile(): ResponseEntity<ResponseModel> {
        val userProfile = userService.getProfile()
        return ResponseEntity.ok(ResponseModel.success(userProfile, "Get user profile successfully!"))
    }

    @Operation(summary = "Update profile")
    @PutMapping("/profile")
    fun updateProfile(@RequestBody req: UpdateUserProfileRequest): ResponseEntity<ResponseModel> {
        val userProfile = userService.updateProfile(req)
        return ResponseEntity.ok(ResponseModel.success(userProfile, "Update user profile successfully!"))
    }

} 