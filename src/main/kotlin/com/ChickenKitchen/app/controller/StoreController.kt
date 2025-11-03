package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateStoreRequest
import com.ChickenKitchen.app.model.dto.request.UpdateStoreRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.ingredient.StoreService
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
@RequestMapping("/api/store")
class StoreController (
    private val storeService: StoreService
){

    @Operation(summary = "Get All Store")
    @GetMapping
    fun getAllStore(
        @RequestParam(name = "size", defaultValue = "0") size: Int,
        @RequestParam(name = "pageNumber", defaultValue = "0") pageNumber: Int,
    ) : ResponseEntity<ResponseModel> {
        val data = if (size <= 0 || pageNumber <= 0) storeService.getAll() else storeService.getAll(pageNumber, size)
        return ResponseEntity.ok(ResponseModel.success(data, "Get All Store successfully"))
    }

    @Operation(summary = "Get Store By Id")
    @GetMapping("/{id}")
    fun getStoreById (@PathVariable id : Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(storeService.getById(id), "Get Store by Id $id successfully"))

    }

    @Operation(summary = "Create Store (Admin Only)")
    @PostMapping
    fun createStore (@RequestBody req: CreateStoreRequest) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(storeService.create(req), "Create Store Successfully"))
    }

    @Operation(summary = "Update Store")
    @PutMapping("/{id}")
    fun updateStore (@RequestBody req : UpdateStoreRequest, @PathVariable id : Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(storeService.update(id, req), "Update Store with $id successfully"))
    }

    @Operation(summary = "Change Store Status")
    @PatchMapping("/{id}")
    fun changeStoreStatus (@PathVariable id : Long) : ResponseEntity<ResponseModel>{
        return ResponseEntity.ok(ResponseModel.success(storeService.changeStatus(id), "Update Store Status Successfully"))
    }

    @Operation(summary = "Delete store with condition")
    @DeleteMapping("/{id}")
    fun delete (@PathVariable id :Long) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(storeService.delete(id), "Delete Store Successfully"))
    }

    @Operation(summary = "Get total stores")
    @GetMapping("/counts")
    fun getStoreCounts(): ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(mapOf("total" to storeService.count()), "Fetched store count"))
}
