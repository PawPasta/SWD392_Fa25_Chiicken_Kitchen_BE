package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.service.user.UserAddressService
import com.ChickenKitchen.app.model.dto.request.CreateUserAddressRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserAddressRequest
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import com.ChickenKitchen.app.model.dto.response.ResponseModel

@RestController
@RequestMapping("/api/addresses")
class UserAddressController(
    private val userAddressService: UserAddressService
) {

    @Operation(summary = "Get all user addresses")
    @GetMapping("")
    fun getUserAddresses(): ResponseEntity<ResponseModel> {
        val addresses = userAddressService.getAll()
        return ResponseEntity.ok(ResponseModel.success(addresses, "Get all addresses successfully!"))
    }

    @Operation(summary = "Get a user address by ID")
    @GetMapping("/{id}")
    fun getUserAddressById(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        val address = userAddressService.getById(id)
        return ResponseEntity.ok(ResponseModel.success(address, "Get address successfully!"))
    }

    @Operation(summary = "Create a new user address")
    @PostMapping("")
    fun createUserAddress(
        @RequestBody req: CreateUserAddressRequest
    ): ResponseEntity<ResponseModel> {
        val address = userAddressService.create(req)
        return ResponseEntity.ok(ResponseModel.success(address, "Create address successfully!"))
    }

    @Operation(summary = "Update a user address by ID")
    @PutMapping("/{id}")
    fun updateUserAddress(
        @PathVariable id: Long,
        @RequestBody req: UpdateUserAddressRequest
    ): ResponseEntity<ResponseModel> {
        val address = userAddressService.update(id, req)
        return ResponseEntity.ok(ResponseModel.success(address, "Update address successfully!"))
    }

    @Operation(summary = "Delete a user address by ID")
    @DeleteMapping("/{id}")
    fun deleteUserAddress(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        userAddressService.delete(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Delete address successfully!"))
    }

    @Operation(summary = "Set a user address as default")
    @PutMapping("/{id}/default")
    fun setDefaultAddress(@PathVariable id: Long): ResponseEntity<ResponseModel> {
        userAddressService.setDefault(id)
        return ResponseEntity.ok(ResponseModel.success(null, "Set default address successfully!"))
    }
}