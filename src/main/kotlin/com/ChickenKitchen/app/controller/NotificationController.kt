package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.MultipleNotificationRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.notification.NotificationService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {

    @Operation(summary = "Send test notification to a device ")
    @PostMapping("/mutiple")
    fun sendToTokenMultiple(@RequestBody req : MultipleNotificationRequest) : ResponseEntity<ResponseModel> {
        return ResponseEntity.ok(ResponseModel.success(notificationService.sendToAllUsers(req), "Send successfully" ))
    }
}