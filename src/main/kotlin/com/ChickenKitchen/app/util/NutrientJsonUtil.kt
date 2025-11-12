package com.ChickenKitchen.app.util

import com.ChickenKitchen.app.model.dto.response.MenuItemNutrientBriefResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object NutrientJsonUtil {
    private val mapper = jacksonObjectMapper()

    fun parse(json: String?): List<MenuItemNutrientBriefResponse> {
        if (json.isNullOrBlank()) return emptyList()
        return runCatching { mapper.readValue<List<MenuItemNutrientBriefResponse>>(json) }
            .getOrElse { emptyList() }
    }

    fun toJson(nutrients: List<MenuItemNutrientBriefResponse>): String {
        if (nutrients.isEmpty()) return "[]"
        return runCatching { mapper.writeValueAsString(nutrients) }
            .getOrElse { "[]" }
    }
}
