package com.ChickenKitchen.app.model.dto.response

data class DishSearchResponse(
    val items: List<DishResponse>,
    val total: Long
)

