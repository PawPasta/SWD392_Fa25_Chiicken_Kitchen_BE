package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.request.AiChatRequest
import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.AI.AiChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ai/chat")
class AiChatController(
    private val aiChatService: AiChatService,
) {

    @Operation(summary = "Send a message to AI and get reply (auto applies order actions if intent=order)")
    @ApiResponse(responseCode = "200", description = "AI replied successfully")
    @PostMapping
    fun chat(@RequestBody req: AiChatRequest): ResponseEntity<ResponseModel> {
        val result = aiChatService.chat(req)
        val answerPayload = result.answerJson ?: result.answerRaw
        val data = mutableMapOf<String, Any?>(
            "answer" to answerPayload,
            "storeId" to req.storeId
        )
        result.orderResult?.let { data["orderResult"] = it }
        return ResponseEntity.ok(ResponseModel.success(data, "AI replied successfully"))
    }

    @Operation(summary = "Get chat history (current session) with pagination")
    @ApiResponse(responseCode = "200", description = "History fetched successfully")
    @GetMapping("/history")
    fun history(
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestParam(name = "size", defaultValue = "20") size: Int,
        @RequestParam(name = "asc", defaultValue = "true") asc: Boolean,
    ): ResponseEntity<ResponseModel> {
        val pageData = aiChatService.getHistory(page, size, asc)
        val items = pageData.content.map {
            mapOf(
                "id" to it.id,
                "role" to it.role,
                "content" to it.content,
                "createdAt" to it.createdAt
            )
        }
        val response = mapOf(
            "items" to items,
            "page" to pageData.number + 1,
            "size" to pageData.size,
            "totalElements" to pageData.totalElements,
            "totalPages" to pageData.totalPages
        )
        return ResponseEntity.ok(ResponseModel.success(response, "History fetched successfully"))
    }
}
