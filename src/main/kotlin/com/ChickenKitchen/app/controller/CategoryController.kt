package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateCategoryRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCategoryRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.category.CategoryService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/category")
class CategoryController(
    private val categoryService: CategoryService
) {

    // Public
    @Operation(summary = "Get all categories")
    @GetMapping("")
    fun getAllCategories(): ResponseEntity<ResponseModel> {
        val categories = categoryService.getAll()
        return ResponseEntity.ok(ResponseModel.success(categories, "Get all categories successfully!"))
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val category = categoryService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(category, "Get category successfully!"))
    }

    // Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new category (Admin only)")
    @PostMapping("")
    fun createCategory(@RequestBody req: CreateCategoryRequest): ResponseEntity<ResponseModel> {
        val category = categoryService.create(req)
        return ResponseEntity.ok(ResponseModel.success(category, "Create category successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category by ID (Admin only)")
    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Long, @RequestBody req: UpdateCategoryRequest): ResponseEntity<ResponseModel> {
        val category = categoryService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(category, "Update category successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete category by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        categoryService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete category successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change category status (Admin only)")
    @PatchMapping("/{id}/status")
    fun changeCategoryStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val category = categoryService.changeStatus(id)
        return ResponseEntity.ok(ResponseModel.success(category, "Change category status successfully!"))
    }
}

