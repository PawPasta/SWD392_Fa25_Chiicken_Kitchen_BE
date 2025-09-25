package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreatePromotionRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePromotionRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.promotion.PromotionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/promotion")
class PromotionController(
    private val promotionService: PromotionService
) {

    // Public
    @Operation(summary = "Get all promotions")
    @GetMapping("")
    fun getAllPromotions(): ResponseEntity<ResponseModel> {
        val promotions = promotionService.getAll()
        return ResponseEntity.ok(ResponseModel.success(promotions, "Get all promotions successfully!"))
    }

    @Operation(summary = "Get promotion by ID")
    @GetMapping("/{id}")
    fun getPromotionById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val promotion = promotionService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(promotion, "Get promotion successfully!"))
    }

    // Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new promotion (Admin only)")
    @PostMapping("")
    fun createPromotion(@RequestBody req: CreatePromotionRequest): ResponseEntity<ResponseModel> {
        val promotion = promotionService.create(req)
        return ResponseEntity.ok(ResponseModel.success(promotion, "Create promotion successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update promotion by ID (Admin only)")
    @PutMapping("/{id}")
    fun updatePromotion(@PathVariable id: Long, @RequestBody req: UpdatePromotionRequest): ResponseEntity<ResponseModel> {
        val promotion = promotionService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(promotion, "Update promotion successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete promotion by ID (Admin only)")
    @DeleteMapping("/{id}")
    fun deletePromotion(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        promotionService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete promotion successfully!"))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change promotion status (Admin only)")
    @PatchMapping("/{id}/status")
    fun changePromotionStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val promotion = promotionService.changeStatus(id)
        return ResponseEntity.ok(ResponseModel.success(promotion, "Change promotion status successfully!"))
    }
}

