package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateCookingMethodRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCookingMethodRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.cooking.CookingMethodService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/cooking-method")
class CookingMethodController(
    private val cookingMethodService: CookingMethodService
) {

    // Mina can access

    @Operation(summary = "Get all cooking methods")
    @GetMapping
    fun getAllCookingMethods(): ResponseEntity<ResponseModel> {
        val methods = cookingMethodService.getAll()
        return ResponseEntity.ok(ResponseModel.success(methods, "Cooking methods retrieved successfully"))
    }

    @Operation(summary = "Get cooking method by ID")
    @GetMapping("/{id}")
    fun getCookingMethodById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val method = cookingMethodService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(method, "Cooking method retrieved successfully"))
    }

    // Chỉ có Admin mới có thể truy cập những endpoint dưới đây.
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new cooking method (Admin only)")
    @PostMapping
    fun createCookingMethod(@RequestBody req: CreateCookingMethodRequest): ResponseEntity<ResponseModel> {
        val method = cookingMethodService.create(req)
        return ResponseEntity.ok(ResponseModel.success(method, "Cooking method created successfully"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update cooking method by ID (Admin only)")
    @PutMapping("/{id}")
    fun updateCookingMethod(@PathVariable id: Long, @RequestBody req: UpdateCookingMethodRequest): ResponseEntity<ResponseModel> {
        val method = cookingMethodService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(method, "Cooking method updated successfully"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete cooking method by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteCookingMethod(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        cookingMethodService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Cooking method deleted successfully"))
    }
}