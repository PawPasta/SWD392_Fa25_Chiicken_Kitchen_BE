package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateDishBaseRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishBaseRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.step.DishService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/dishes")
class DishController(
    private val dishService: DishService
) {

    @Operation(summary = "Get all dishes (paginated)")
    @GetMapping
    fun getAll(
        @RequestParam(name = "size", defaultValue = "0") size: Int,
        @RequestParam(name = "pageNumber", defaultValue = "0") pageNumber: Int,
    ): ResponseEntity<ResponseModel> {
        val data = if (size <= 0 || pageNumber <= 0) dishService.getAll() else dishService.getAll(pageNumber, size)
        return ResponseEntity.ok(ResponseModel.success(data, "Fetched dishes"))
    }

    @Operation(summary = "Get dish by id")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(dishService.getById(id), "Fetched dish"))

    @Operation(summary = "Create dish")
    @PostMapping
    fun create(@RequestBody req: CreateDishBaseRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(dishService.create(req), "Dish created"))

    @Operation(summary = "Update dish")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: UpdateDishBaseRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(dishService.update(id, req), "Dish updated"))

    @Operation(summary = "Delete dish")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        dishService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Dish deleted"))
    }

    @Operation(summary = "Get total dishes")
    @GetMapping("/counts")
    fun getCounts(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(mapOf("total" to dishService.count()), "Fetched dish count"))

    @Operation(summary = "Get my custom dishes (requires auth)")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/custom/mine")
    fun getMyCustomDishes(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(dishService.getMyCustomDishes(), "Fetched my custom dishes"))

    @Operation(summary = "Search sample dishes (components + calories + price), paginated with total")
    @GetMapping("/search")
    fun search(
        @RequestParam(name = "menuItemIds", required = false) menuItemIds: List<Long>?,
        @RequestParam(name = "keyword", required = false) keyword: String?,
        @RequestParam(name = "minCal", required = false) minCal: Int?,
        @RequestParam(name = "maxCal", required = false) maxCal: Int?,
        @RequestParam(name = "minPrice", required = false) minPrice: Int?,
        @RequestParam(name = "maxPrice", required = false) maxPrice: Int?,
        @RequestParam(name = "size", defaultValue = "10") size: Int,
        @RequestParam(name = "pageNumber", defaultValue = "1") pageNumber: Int,
    ): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(
            ResponseModel.success(
                dishService.search(menuItemIds, keyword, minCal, maxCal, minPrice, maxPrice, pageNumber, size),
                "Fetched dishes"
            )
        )
}
