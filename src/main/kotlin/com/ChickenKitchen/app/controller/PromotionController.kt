package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreatePromotionRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePromotionRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.promotion.PromotionService
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
@RequestMapping("/api/promotion")
class PromotionController (
    private val promotionService: PromotionService
) {

    @Operation(summary = "Get List promotions (all actor)")
    @GetMapping
    fun getAll(
        @RequestParam(name = "size", defaultValue = "0") size: Int,
        @RequestParam(name = "pageNumber", defaultValue = "0") pageNumber: Int,
    ) : ResponseEntity<ResponseModel> {
        val data = if (size <= 0 || pageNumber <= 0) promotionService.getAll() else promotionService.getAll(pageNumber, size)
        return ResponseEntity.ok(ResponseModel.success(data, "Get Promotion Successfully"))
    }

    @Operation(summary = "Get promotions by Ids ")
    @GetMapping("/{id}")
    fun getById (@PathVariable id : Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(promotionService.getById(id),"Get Promotion By Id Success"))
    }

    @Operation(summary = "Create Promotions (Manager only)")
    @PostMapping
    fun create (@RequestBody req : CreatePromotionRequest) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(promotionService.create(req), "Create Promotion Successfully"))
    }

    @Operation(summary = "Update Promotions (Manager only)")
    @PutMapping("/{id}")
    fun update (@RequestBody req : UpdatePromotionRequest, @PathVariable id: Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(promotionService.update(id,req),"Updated Promotion Successfully" ))
    }

    @Operation(summary = "Update Promotion Status (Manager only)")
    @PatchMapping("/{id}")
    fun changeStatus (@PathVariable id: Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(promotionService.changeStatus(id),"Change Promotion Staus Successfully"))
    }

    @Operation(summary = "Delete Promotion with condition")
    @DeleteMapping("/{id}")
    fun delete (@PathVariable id :Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(promotionService.delete(id), "Delete Promotion Successfully"))
    }

    @Operation(summary = "Get total promotions")
    @GetMapping("/counts")
    fun getCounts(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(mapOf("total" to promotionService.count()), "Fetched promotion count"))
}
