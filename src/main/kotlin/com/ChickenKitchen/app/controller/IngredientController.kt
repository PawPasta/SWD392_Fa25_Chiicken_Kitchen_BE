package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateIngredientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateIngredientRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.ingredient.IngredientService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam


@RestController
@RequestMapping("/api/ingredient")
class IngredientController (

    private val ingredientService: IngredientService,
){

    @Operation(summary = "Get All Ingredients")
    @GetMapping
    fun getAllIngredients(
        @RequestParam(name = "size", defaultValue = "10") size: Int,
        @RequestParam(name = "pageNumber", defaultValue = "1") pageNumber: Int,
    ): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(
            ResponseModel.success(
                ingredientService.getAll(pageNumber, size),
                "Get All Ingredients successfully"
            )
        )
    }

    @Operation(summary = "Get Ingredient Detail By ID (Only Manager)")
    @GetMapping("/{id}")
    fun getIngredientById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(
            ResponseModel.success(
                ingredientService.getById(id),
                "Get Ingredient detail successfully"
            )
        )
    }

    @Operation(summary = "Create Ingredient (Only Manager)")
    @PostMapping
    fun createIngredient(@RequestBody req: CreateIngredientRequest): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(
            ResponseModel.success(
                ingredientService.create(req),
                "Create Ingredient successfully"
            )
        )
    }

    @Operation(summary = "Update Ingredient (Only Manager)")
    @PutMapping("/{id}")
    fun updateIngredient(
        @PathVariable id: Long,
        @RequestBody req: UpdateIngredientRequest
    ): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(
            ResponseModel.success(
                ingredientService.update(id, req),
                "Update Ingredient successfully"
            )
        )
    }

    @Operation(summary = "Change Ingredient Status (Only Manager)")
    @PatchMapping("/{id}")
    fun changeIngredientStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(
            ResponseModel.success(
                ingredientService.changeStatus(id),
                "Change Ingredient status successfully"
            )
        )
    }

    @Operation(summary = "Delete Ingredient")
    @DeleteMapping("/{id}")
    fun deleteIngredient(@PathVariable id: Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(ingredientService.delete(id), "Delete Ingredient Successfully"))
    }

    @Operation(summary = "Get total Ingredients")
    @GetMapping("/counts")
    fun getIngredientCounts(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(mapOf("total" to ingredientService.count()), "Fetched ingredient count"))


}
