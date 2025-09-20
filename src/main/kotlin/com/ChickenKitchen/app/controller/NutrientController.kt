package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.service.ingredient.NutrientService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.http.ResponseEntity
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.model.dto.request.CreateNutrientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateNutrientRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/nutrient")
class NutrientController( 
    private val nutrientService: NutrientService
) {

    // Chỉ có Admin mới có thể truy cập những endpoint này.

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all nutrients (Admin only)")
    @GetMapping("")
    fun getAllUsers(): ResponseEntity<ResponseModel> {
        val nutrients = nutrientService.getAll()
        return ResponseEntity.ok(ResponseModel.success(nutrients, "Get all nutrients successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get nutrient by ID (Admin only)")
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val nutrient = nutrientService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(nutrient, "Get nutrient successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new nutrient (Admin only)") 
    @PostMapping("")
    fun createUser(@RequestBody req: CreateNutrientRequest): ResponseEntity<ResponseModel> {
        val nutrient = nutrientService.create(req)
        return ResponseEntity.ok(ResponseModel.success(nutrient, "Create nutrient successfully!"))
    } 

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update nutrient by ID (Admin only)")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody req: UpdateNutrientRequest): ResponseEntity<ResponseModel> {
        val nutrient = nutrientService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(nutrient, "Update nutrient successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete nutrient by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        nutrientService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete nutrient successfully!"))
    }


} 