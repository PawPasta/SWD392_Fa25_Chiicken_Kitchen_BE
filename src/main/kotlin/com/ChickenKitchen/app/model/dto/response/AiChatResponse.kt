package com.ChickenKitchen.app.model.dto.response

import com.fasterxml.jackson.databind.JsonNode

data class AiChatResponse(
    val answerRaw: String,
    val answerJson: JsonNode?,
    val orderResult: Map<String, Any>?
)

