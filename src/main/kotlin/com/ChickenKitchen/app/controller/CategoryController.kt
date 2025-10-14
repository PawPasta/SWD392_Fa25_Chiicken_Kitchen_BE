package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.CreateCategoryRequest
import com.ChickenKitchen.app.model.dto.request.UpdateCategoryRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.category.CategoryService
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


@RestController
@RequestMapping("/api/category")
class CategoryController(
    private val categoryService: CategoryService
) {

    @Operation(summary = "Get all category (Manager Only)")
    @GetMapping()
    fun getAll() : ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(categoryService.getAll(),"Fetched Category"))

    @Operation(summary = "Get Category by Id (Manager Only)")
    @GetMapping("/{id}")
    fun getById (@PathVariable id : Long) : ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(categoryService.getById(id),"Fetched Category by Id"))

    @Operation(summary = "Create Category")
    @PostMapping
    fun create (@RequestBody req : CreateCategoryRequest) : ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(categoryService.create(req), "Created New Category Successfully"))

    @Operation(summary = "Update Category (Manager Only)")
    @PutMapping("/{id}")
    fun update (@RequestBody req: UpdateCategoryRequest, @PathVariable id : Long) : ResponseEntity<ResponseModel> =
        ResponseEntity.ok(ResponseModel.success(categoryService.update(id, req), "Updated Category Successfully"))


    // THằng cu delete này tạm để đây, mốt cập nhật logic sau
//    @Operation(summary = "Delete Category (Manager Only)")
//    @DeleteMapping("/{id}")
//    fun delete (@PathVariable id : Long) : ResponseEntity<ResponseModel> =
//        ResponseEntity.ok(ResponseModel.success(categoryService.delete(id), "Delete successfully"))


}