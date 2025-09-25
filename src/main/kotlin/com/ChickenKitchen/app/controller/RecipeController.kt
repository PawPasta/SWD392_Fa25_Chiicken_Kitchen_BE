package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateRecipeRequest
import com.ChickenKitchen.app.model.dto.request.UpdateRecipeRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.recipe.RecipeService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipe")
class RecipeController(
    private val recipeService: RecipeService
) {

    // Public endpoints
    @Operation(summary = "Get all recipes")
    @GetMapping("")
    fun getAll(): ResponseEntity<ResponseModel> {
        val recipes = recipeService.getAll()
        return ResponseEntity.ok(ResponseModel.success(recipes, "Get all recipes successfully!"))
    }

    @Operation(summary = "Get recipe by ID")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val recipe = recipeService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(recipe, "Get recipe successfully!"))
    }

    // Admin-only endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new recipe (Admin only)")
    @PostMapping("")
    fun create(@RequestBody req: CreateRecipeRequest): ResponseEntity<ResponseModel> {
        val created = recipeService.create(req)
        return ResponseEntity.ok(ResponseModel.success(created, "Create recipe successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update recipe (Admin only)")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: UpdateRecipeRequest): ResponseEntity<ResponseModel> {
        val updated = recipeService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(updated, "Update recipe successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete recipe (Admin only)")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        recipeService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete recipe successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change recipe status (Admin only)")
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val recipe = recipeService.changeStatus(id)
        return ResponseEntity.ok(ResponseModel.success(recipe, "Change recipe status successfully!"))
    }
}

