package com.ChickenKitchen.app.model.dto.response
import com.ChickenKitchen.app.enums.UnitType
import java.math.BigDecimal

data class MenuItemResponse(
    val id: Long,
    val name: String,
    val categoryId: Long,
    val categoryName: String,
    val isActive: Boolean,
    val imageUrl: String?,
    val price: Int,
    val cal: Int,
    val description: String?,
)

data class MenuItemNutrientBriefResponse(
    val id: Long, // nutrient id
    val name: String,
    val quantity: BigDecimal,
    val baseUnit: UnitType
)

data class MenuItemDetailResponse(
    val id: Long,
    val name: String,
    val categoryId: Long,
    val categoryName: String,
    val isActive: Boolean,
    val imageUrl: String?,
    val createdAt: String,
    val price: Int,
    val cal: Int,
    val description: String?,
    val nutrients: List<MenuItemNutrientBriefResponse> = emptyList(),
    val recipe: List<RecipeBriefResponse> = emptyList(),
)


data class RecipeBriefResponse(
    val id: Long,
    val name: String?,

)