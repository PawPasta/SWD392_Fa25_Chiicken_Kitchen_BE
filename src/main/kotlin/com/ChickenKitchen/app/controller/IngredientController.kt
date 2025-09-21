package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.service.ingredient.IngredientService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.RequestBody
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

    // Public mọi nhà

    @Operation(summary = "Get all ingredients")
    @GetMapping("")
    fun getAllIngredients(): ResponseEntity<ResponseModel> {
        val ingredients = ingredientService.getAll()
        return ResponseEntity.ok(ResponseModel.success(ingredients, "Get all ingredients successfully!"))
    }

    @Operation(summary = "Get ingredient by ID") // Lấy thông tin ingredient kèm nutrient
    @GetMapping("/{id}")
    fun getIngredientById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val ingredient = ingredientService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(ingredient, "Get ingredient successfully!"))
    }

    // Chỉ có Admin mới có thể truy cập những endpoint này.

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new ingredient (Admin only)") // Có thể tạo ingredient và thêm mối liên hệ với nutrient cùng lúc
    @PostMapping("")
    fun createIngredient(@RequestBody req: CreateIngredientRequest): ResponseEntity<ResponseModel> {
        val ingredient = ingredientService.create(req)
        return ResponseEntity.ok(ResponseModel.success(ingredient, "Create ingredient successfully!"))
    } 

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update ingredient by ID (Admin only)") // Hoặc là tạo xong thì thêm sau ở đây
    @PutMapping("/{id}")
    fun updateIngredient(@PathVariable id: Long, @RequestBody req: UpdateIngredientRequest): ResponseEntity<ResponseModel> {
        val ingredient = ingredientService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(ingredient, "Update ingredient successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete ingredient by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteIngredient(@PathVariable id: Long): ResponseEntity<ResponseModel> {
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