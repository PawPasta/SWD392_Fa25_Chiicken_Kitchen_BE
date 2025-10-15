package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateMenuItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateMenuItemRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.menu.MenuItemService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/menu-items")
class MenuItemController(
    private val menuItemService: MenuItemService
) {
    @Operation(summary = "Get all menu items (all actor)")
    @GetMapping
    fun getAll(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(menuItemService.getAll(), "Fetched menu items"))

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
}

