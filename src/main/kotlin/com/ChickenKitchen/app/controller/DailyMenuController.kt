package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.menu.DailyMenuService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam


@RestController
@RequestMapping("/api/daily-menu")
class DailyMenuController(
    private val dailyMenuService: DailyMenuService
) {

    @Operation(summary = "Get All Daily Menu")
    @GetMapping
    fun getAll(): ResponseEntity<ResponseModel> {
        val list = dailyMenuService.getAll()
        return ResponseEntity.ok(
            ResponseModel.success(list, "Get All Daily Menu Successfully")
        )
    }

    @Operation(summary = "Get Daily Menu By ID")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(
            ResponseModel.success(dailyMenuService.getById(id), "Get Daily Menu Successfully")
        )
    }

    @Operation(summary = "Create Daily Menu")
    @PostMapping
    fun create(@RequestBody req: CreateDailyMenuRequest): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(dailyMenuService.create(req), "Daily Menu Created Successfully"))
    }

    @Operation(summary = "Update Daily Menu")
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody req: UpdateDailyMenuRequest
    ): ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(
            ResponseModel.success( dailyMenuService.update(id, req), "Daily Menu Updated Successfully")
        )
    }

    @Operation(summary = "Delete daiLy menu with condition")
    @DeleteMapping("/{id}")
    fun delete (@PathVariable id :Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(dailyMenuService.delete(id), "Delete Menu Item Successfully"))
    }

    @Operation(summary = "Get daily menu by date for store with grouped categories")
    @GetMapping("/store/{storeId}")
    fun getByStoreAndDate(
        @PathVariable storeId: Long,
        @RequestParam date: String
    ): ResponseEntity<ResponseModel> {
        val result = dailyMenuService.getByStoreAndDate(storeId, date)
        return ResponseEntity.ok(ResponseModel.success(result, "Fetched daily menu for store $storeId on $date"))
    }
}
