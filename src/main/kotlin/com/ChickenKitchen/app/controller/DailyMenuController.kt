package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.menu.DailyMenuService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/daily-menu")
class DailyMenuController(
    private val dailyMenuService: DailyMenuService
) {

    // Public endpoints
    @Operation(summary = "Get all daily menus")
    @GetMapping("")
    fun getAllDailyMenus(): ResponseEntity<ResponseModel> {
        val data = dailyMenuService.getAll()
        return ResponseEntity.ok(ResponseModel.success(data, "Get all daily menus successfully!"))
    }

    @Operation(summary = "Get daily menu by ID")
    @GetMapping("/{id}")
    fun getDailyMenuById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val data = dailyMenuService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(data, "Get daily menu successfully!"))
    }

    // Admin-only
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new daily menu (Admin only)")
    @PostMapping("")
    fun createDailyMenu(@RequestBody req: CreateDailyMenuRequest): ResponseEntity<ResponseModel> {
        val created = dailyMenuService.create(req)
        return ResponseEntity.ok(ResponseModel.success(created, "Create daily menu successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update daily menu (Admin only)")
    @PutMapping("/{id}")
    fun updateDailyMenu(@PathVariable id: Long, @RequestBody req: UpdateDailyMenuRequest): ResponseEntity<ResponseModel> {
        val updated = dailyMenuService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(updated, "Update daily menu successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete daily menu (Admin only)")
    @DeleteMapping("/{id}")
    fun deleteDailyMenu(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        dailyMenuService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete daily menu successfully!"))
    }
}

