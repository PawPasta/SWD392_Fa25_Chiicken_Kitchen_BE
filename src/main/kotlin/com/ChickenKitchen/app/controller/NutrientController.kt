package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateNutrientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateNutrientRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.menu.NutrientService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/nutrients")
class NutrientController(
    private val nutrientService: NutrientService
) {

    @Operation(summary = "Get all nutrients (all actor)")
    @GetMapping
    fun getAll(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(nutrientService.getAll(), "Fetched nutrients"))

    @Operation(summary = "Get nutrient by id (all actor)")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(nutrientService.getById(id), "Fetched nutrient"))

    @Operation(summary = "Create new nutrient (manager only)" )
    @PostMapping
    fun create(@RequestBody req: CreateNutrientRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(nutrientService.create(req), "Nutrient created"))

    @Operation(summary = "Update nutrient (manager only)")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: UpdateNutrientRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(nutrientService.update(id, req), "Nutrient updated"))

    @Operation(summary = "Delete nutrient (manager only)")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        nutrientService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Nutrient deleted"))
    }
}

