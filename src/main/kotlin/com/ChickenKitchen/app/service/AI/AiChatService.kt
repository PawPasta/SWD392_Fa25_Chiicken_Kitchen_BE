package com.ChickenKitchen.app.service.AI

import com.ChickenKitchen.app.model.dto.request.AiChatRequest
import com.ChickenKitchen.app.model.dto.response.AiChatResponse
import com.ChickenKitchen.app.model.entity.ai.ChatMessage
import org.springframework.data.domain.Page

interface AiChatService {
    fun chat(req: AiChatRequest): AiChatResponse
    fun getHistory(): List<ChatMessage>
    fun getHistory(page: Int, size: Int, asc: Boolean = true): Page<ChatMessage>
}
