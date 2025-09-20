package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.service.ingredient.IngredientService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.http.ResponseEntity
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.model.dto.request.UpdateIngredientRequest
import com.ChickenKitchen.app.model.dto.request.CreateIngredientRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/ingredient")
class IngredientController(
    private val ingredientService: IngredientService
) {

    // Chỉ có Admin mới có thể truy cập những endpoint này.

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all ingredients (Admin only)")
    @GetMapping("")
    fun getAllUsers(): ResponseEntity<ResponseModel> {
        val ingredients = ingredientService.getAll()
        return ResponseEntity.ok(ResponseModel.success(ingredients, "Get all ingredients successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get ingredient by ID (Admin only)")
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val ingredient = ingredientService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(ingredient, "Get ingredient successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new ingredient (Admin only)") 
    @PostMapping("")
    fun createUser(@RequestBody req: CreateIngredientRequest): ResponseEntity<ResponseModel> {
        val ingredient = ingredientService.create(req)
        return ResponseEntity.ok(ResponseModel.success(ingredient, "Create ingredient successfully!"))
    } 

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update ingredient by ID (Admin only)")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody req: UpdateIngredientRequest): ResponseEntity<ResponseModel> {
        val ingredient = ingredientService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(ingredient, "Update ingredient successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete ingredient by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        ingredientService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete ingredient successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change ingredient status by ID (Admin only)")
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val ingredient = ingredientService.changeStatus(id)
        return ResponseEntity.ok(ResponseModel.success(ingredient, "Change ingredient status successfully!"))
    }

} 