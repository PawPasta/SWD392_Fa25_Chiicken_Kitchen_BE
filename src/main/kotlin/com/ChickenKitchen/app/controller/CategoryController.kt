package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateCategoryRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCategoryRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.category.CategoryService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    @Operation(summary = "Get all categories (all actor)")
    @GetMapping
    fun getAll(
        @RequestParam(name = "size", defaultValue = "0") size: Int,
        @RequestParam(name = "pageNumber", defaultValue = "0") pageNumber: Int,
    ): ResponseEntity<ResponseModel> {
        val data = if (size <= 0 || pageNumber <= 0) categoryService.getAll() else categoryService.getAll(pageNumber, size)
        return ResponseEntity.ok(ResponseModel.success(data, "Fetched categories"))
    }

    @Operation(summary = "Get category by id (all actor)")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(categoryService.getById(id), "Fetched category"))

    @Operation(summary = "Create a new category (manager only)")
    @PostMapping
    fun create(@RequestBody req: CreateCategoryRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(categoryService.create(req), "Category created"))

    @Operation(summary = "Update category (manager only)")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: UpdateCategoryRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(categoryService.update(id, req), "Category updated"))

    @Operation(summary = "Delete category (manager only)")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        categoryService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Category deleted"))
    }

    @Operation(summary = "Get total categories (manager only)")
    @GetMapping("/counts")
    fun getCounts(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(mapOf("total" to categoryService.count()), "Fetched category count"))
}
