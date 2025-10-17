package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateStepRequest
import com.ChickenKitchen.app.model.dto.request.StepOrderRequest
import com.ChickenKitchen.app.model.dto.request.UpdateStepRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.step.StepService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/steps")
class StepController(
    private val stepService: StepService
) {

    // Logged users can view
    @Operation(summary = "Get all steps (LOGGED)")
    @GetMapping
    fun getAll(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(stepService.getAll(), "Fetched steps"))

    @Operation(summary = "Get step by id (LOGGED)")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(stepService.getById(id), "Fetched step"))

    // Manager-only operations
    @Operation(summary = "Create step (MANAGER)")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    fun create(@RequestBody req: CreateStepRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(stepService.create(req), "Step created"))

    @Operation(summary = "Update step (MANAGER)")
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: UpdateStepRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(stepService.update(id, req), "Step updated"))

    @Operation(summary = "Delete step (MANAGER)")
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        stepService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Step deleted"))
    }

    @Operation(summary = "Change step order (MANAGER)")
    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{id}/order")
    fun changeOrder(@PathVariable id: Long, @RequestBody req: StepOrderRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(stepService.changeOrder(id, req), "Step order updated"))
}

