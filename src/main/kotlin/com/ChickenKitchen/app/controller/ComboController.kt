package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateComboRequest
import com.ChickenKitchen.app.model.dto.request.UpdateComboRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.combo.ComboService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/combo")
class ComboController(
    private val comboService: ComboService
) {

    // Public
    @Operation(summary = "Get all combos")
    @GetMapping("")
    fun getAllCombos(): ResponseEntity<ResponseModel> {
        val combos = comboService.getAll()
        return ResponseEntity.ok(ResponseModel.success(combos, "Get all combos successfully!"))
    }

    @Operation(summary = "Get combo by ID")
    @GetMapping("/{id}")
    fun getComboById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val combo = comboService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(combo, "Get combo successfully!"))
    }

    // Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new combo (Admin only)")
    @PostMapping("")
    fun createCombo(@RequestBody req: CreateComboRequest): ResponseEntity<ResponseModel> {
        val combo = comboService.create(req)
        return ResponseEntity.ok(ResponseModel.success(combo, "Create combo successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update combo by ID (Admin only)")
    @PutMapping("/{id}")
    fun updateCombo(@PathVariable id: Long, @RequestBody req: UpdateComboRequest): ResponseEntity<ResponseModel> {
        val combo = comboService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(combo, "Update combo successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete combo by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteCombo(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        comboService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete combo successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change combo status (Admin only)")
    @PatchMapping("/{id}/status")
    fun changeComboStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val combo = comboService.changeStatus(id)
        return ResponseEntity.ok(ResponseModel.success(combo, "Change combo status successfully!"))
    }
}

