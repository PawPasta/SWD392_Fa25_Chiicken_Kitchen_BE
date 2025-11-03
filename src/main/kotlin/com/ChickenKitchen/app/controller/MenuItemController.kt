package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateMenuItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateMenuItemRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.menu.MenuItemService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api/menu-items")
class MenuItemController(
    private val menuItemService: MenuItemService
) {
    @Operation(summary = "Get all menu items (all actor)")
    @GetMapping
    fun getAll(
        @RequestParam(name = "size", defaultValue = "0") size: Int,
        @RequestParam(name = "pageNumber", defaultValue = "0") pageNumber: Int,
    ): ResponseEntity<ResponseModel> {
        val data = if (size <= 0 || pageNumber <= 0) menuItemService.getAll() else menuItemService.getAll(pageNumber, size)
        return ResponseEntity.ok(ResponseModel.success(data, "Fetched menu items"))
    }

    @Operation(summary = "Get menu item by id (all actor)")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(menuItemService.getById(id), "Fetched menu item"))

    @Operation(summary = "Create new menu item (manager only)")
    @PostMapping
    fun create(@RequestBody req: CreateMenuItemRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(menuItemService.create(req), "Menu item created"))

    @Operation(summary = "Update menu item (manager only)")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: UpdateMenuItemRequest): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(menuItemService.update(id, req), "Menu item updated"))

    @Operation(summary = "Delete menu item (manager only)")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        menuItemService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Menu item deleted"))
    }

    @Operation(summary = "Toggle menu item status (manager only)")
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable id: Long): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(menuItemService.changeStatus(id), "Status toggled"))

    @Operation(summary = "Get total menu items (manager only)")
    @GetMapping("/counts")
    fun getCounts(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(mapOf("total" to menuItemService.count()), "Fetched menu items count"))

    @Operation(summary = "Search menu items by name/category/price/cal with pagination and total")
    @GetMapping("/search")
    fun search(
        @RequestParam(name = "name", required = false) name: String?,
        @RequestParam(name = "categoryId", required = false) categoryId: Long?,
        @RequestParam(name = "minPrice", required = false) minPrice: Int?,
        @RequestParam(name = "maxPrice", required = false) maxPrice: Int?,
        @RequestParam(name = "minCal", required = false) minCal: Int?,
        @RequestParam(name = "maxCal", required = false) maxCal: Int?,
        @RequestParam(name = "size", defaultValue = "10") size: Int,
        @RequestParam(name = "pageNumber", defaultValue = "1") pageNumber: Int,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") sortBy: String,
        @RequestParam(name = "direction", defaultValue = "desc") direction: String,
    ): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(
            ResponseModel.success(
                menuItemService.searchPaged(name, categoryId, minPrice, maxPrice, minCal, maxCal, pageNumber, size, sortBy, direction),
                "Fetched menu items with total"
            )
        )
}
