package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateUserRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserProfileRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserRequest
import com.ChickenKitchen.app.model.dto.request.GrantRoleRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.user.UserService
import com.ChickenKitchen.app.enums.Role
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    // ===================== MANAGER: CRUD =====================

    @Operation(summary = "Get all users (ADMIN)")
    // @PreAuthorize("hasRole('ADMIN')") // Temporarily public
    @GetMapping
    fun getAllUsers(
        @RequestParam(name = "size", defaultValue = "10") size: Int,
        @RequestParam(name = "pageNumber", defaultValue = "1") pageNumber: Int,
    ): ResponseEntity<ResponseModel> {
        val users = userService.getAll(pageNumber = pageNumber, size = size)
        return ResponseEntity.ok(ResponseModel.success(users, "Fetched users successfully"))
    }

    @Operation(summary = "Get user by id (ADMIN)")
    // @PreAuthorize("hasRole('ADMIN')") // Temporarily public
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val user = userService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(user, "Fetched user successfully"))
    }

    @Operation(summary = "Create new user (ADMIN)")
    // @PreAuthorize("hasRole('ADMIN')") // Temporarily public
    @PostMapping
    fun createUser(@RequestBody req: CreateUserRequest): ResponseEntity<ResponseModel> {
        val created = userService.create(req)
        return ResponseEntity.ok(ResponseModel.success(created, "User created successfully"))
    }

    @Operation(summary = "Update user by id (ADMIN)")
    // @PreAuthorize("hasRole('ADMIN')") // Temporarily public
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody req: UpdateUserRequest): ResponseEntity<ResponseModel> {
        val updated = userService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(updated, "User updated successfully"))
    }

    @Operation(summary = "Delete user by id (ADMIN)")
    // @PreAuthorize("hasRole('ADMIN')") // Temporarily public
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        userService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "User deleted successfully"))
    }

    @Operation(summary = "Toggle user status (ADMIN)")
    // @PreAuthorize("hasRole('ADMIN')") // Temporarily public
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val result = userService.changeStatus(id)
        return ResponseEntity.ok(ResponseModel.success(result, "User status updated successfully"))
    }

    // ===================== USER: Profile =====================
    @Operation(summary = "Get current user profile (LOGGED)")
    @GetMapping("/me")
    fun getProfile(): ResponseEntity<ResponseModel> {
        val profile = userService.getProfile()
        return ResponseEntity.ok(ResponseModel.success(profile, "Fetched profile successfully"))
    }

    @Operation(summary = "Update current user profile (LOGGED)")
    @PutMapping("/me")
    fun updateProfile(@RequestBody req: UpdateUserProfileRequest): ResponseEntity<ResponseModel> {
        val profile = userService.updateProfile(req)
        return ResponseEntity.ok(ResponseModel.success(profile, "Profile updated successfully"))
    }

    @Operation(summary = "Grant role to email (ADMIN)")
    // @PreAuthorize("hasRole('ADMIN')") // Temporarily public
    @PostMapping("/grant-role")
    fun grantRoleToEmail(@RequestBody req: GrantRoleRequest): ResponseEntity<ResponseModel> {
        val result = userService.grantRoleByEmail(req)
        return ResponseEntity.ok(ResponseModel.success(result, "Role granted/updated successfully"))
    }

    @Operation(summary = "Get all roles")
    // @PreAuthorize("hasRole('ADMIN')") // Temporarily public
    @GetMapping("/roles")
    fun getAllRoles(): ResponseEntity<ResponseModel> {
        val roles: List<Role> = userService.getAllRoles()
        return ResponseEntity.ok(ResponseModel.success(roles, "Fetched roles successfully"))
    }

    @Operation(summary = "Get total users and counts by role (ADMIN)")
    // @PreAuthorize("hasRole('ADMIN')") // Temporarily public
    @GetMapping("/counts")
    fun getUserCounts(): ResponseEntity<ResponseModel> {
        val counts = userService.getCounts()
        return ResponseEntity.ok(ResponseModel.success(counts, "Fetched user counts successfully"))
    }
}
