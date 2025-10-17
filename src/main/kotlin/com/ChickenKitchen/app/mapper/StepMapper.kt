package com.ChickenKitchen.app.mapper

import com.ChickenKitchen.app.model.entity.step.Step
import com.ChickenKitchen.app.model.dto.response.StepResponse
import com.ChickenKitchen.app.model.dto.response.StepDetailResponse

fun Step.toStepResponse(): StepResponse =
    StepResponse(
        id = this.id!!,
        name = this.name,
        description = this.description,
        categoryId = this.category.id!!,
        categoryName = this.category.name,
        stepNumber = this.stepNumber,
        isActive = this.isActive,
    )

fun List<Step>.toStepResponseList(): List<StepResponse> =
    this.map { it.toStepResponse() }

fun Step.toStepDetailResponse(): StepDetailResponse =
    StepDetailResponse(
        id = this.id!!,
        name = this.name,
        description = this.description,
        categoryId = this.category.id!!,
        categoryName = this.category.name,
        stepNumber = this.stepNumber,
        isActive = this.isActive,
    )

