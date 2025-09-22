package com.ChickenKitchen.app.service.recipe

import com.ChickenKitchen.app.model.dto.request.CreateRecipeRequest
import com.ChickenKitchen.app.model.dto.request.UpdateRecipeRequest
import com.ChickenKitchen.app.model.dto.response.RecipeResponse
import com.ChickenKitchen.app.model.dto.response.RecipeDetailResponse
import com.ChickenKitchen.app.service.BaseService

interface RecipeService : BaseService<RecipeResponse, RecipeDetailResponse, 
CreateRecipeRequest, UpdateRecipeRequest, Long>

