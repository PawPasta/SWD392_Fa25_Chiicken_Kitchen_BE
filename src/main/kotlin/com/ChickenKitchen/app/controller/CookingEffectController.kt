package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.model.dto.request.CreateCookingEffectRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCookingEffectRequest
import com.ChickenKitchen.app.service.cooking.CookingEffectService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/cooking-effect")
class CookingEffectController(
    private val cookingEffectService: CookingEffectService
) {

    // Mina can access

    @Operation(summary = "Get all cooking effects")
    @GetMapping
    fun getAllCookingEffects(): ResponseEntity<ResponseModel> {
        val effects = cookingEffectService.getAll()
        return ResponseEntity.ok(ResponseModel.success(effects, "Cooking effects retrieved successfully"))
    }

    @Operation(summary = "Get cooking effect by ID")
    @GetMapping("/{id}")
    fun getCookingEffectById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val effect = cookingEffectService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(effect, "Cooking effect retrieved successfully"))
    }

    // Chỉ có Admin mới có thể truy cập những endpoint dưới đây.
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new cooking effect (Admin only)")
    @PostMapping
    fun createCookingEffect(@RequestBody req: CreateCookingEffectRequest): ResponseEntity<ResponseModel> {
        val effect = cookingEffectService.create(req)
        return ResponseEntity.ok(ResponseModel.success(effect, "Cooking effect created successfully"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update cooking effect by ID (Admin only)")
    @PutMapping("/{id}")
    fun updateCookingEffect(@PathVariable id: Long, @RequestBody req: UpdateCookingEffectRequest): ResponseEntity<ResponseModel> {
        val effect = cookingEffectService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(effect, "Cooking effect updated successfully"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete cooking effect by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteCookingEffect(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        cookingEffectService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Cooking effect deleted successfully"))
    }
}