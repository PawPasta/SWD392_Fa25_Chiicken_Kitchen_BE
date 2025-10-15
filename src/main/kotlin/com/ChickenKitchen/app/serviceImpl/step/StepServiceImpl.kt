package com.ChickenKitchen.app.serviceImpl.step

import com.ChickenKitchen.app.mapper.toStepDetailResponse
import com.ChickenKitchen.app.mapper.toStepResponse
import com.ChickenKitchen.app.mapper.toStepResponseList
import com.ChickenKitchen.app.model.dto.request.CreateStepRequest
import com.ChickenKitchen.app.model.dto.request.StepOrderRequest
import com.ChickenKitchen.app.model.dto.request.UpdateStepRequest
import com.ChickenKitchen.app.model.dto.response.StepDetailResponse
import com.ChickenKitchen.app.model.dto.response.StepResponse
import com.ChickenKitchen.app.model.entity.step.Step
import com.ChickenKitchen.app.repository.category.CategoryRepository
import com.ChickenKitchen.app.repository.step.StepRepository
import com.ChickenKitchen.app.service.step.StepService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StepServiceImpl(
    private val stepRepository: StepRepository,
    private val categoryRepository: CategoryRepository,
) : StepService {

    override fun getAll(): List<StepResponse>? {
        val steps = stepRepository.findAll().sortedBy { it.stepNumber }
        if (steps.isEmpty()) return null
        return steps.toStepResponseList()
    }

    override fun getById(id: Long): StepDetailResponse {
        val step = stepRepository.findById(id).orElseThrow { NoSuchElementException("Step with id $id not found") }
        return step.toStepDetailResponse()
    }

    override fun create(req: CreateStepRequest): StepDetailResponse {
        val category = categoryRepository.findById(req.categoryId)
            .orElseThrow { NoSuchElementException("Category with id ${req.categoryId} not found") }
        val entity = Step(
            category = category,
            name = req.name,
            description = req.description,
            isActive = req.isActive,
            stepNumber = req.stepNumber,
        )
        val saved = stepRepository.save(entity)
        return saved.toStepDetailResponse()
    }

    override fun update(id: Long, req: UpdateStepRequest): StepDetailResponse {
        val current = stepRepository.findById(id).orElseThrow { NoSuchElementException("Step with id $id not found") }
        val category = if (req.categoryId != null) categoryRepository.findById(req.categoryId)
            .orElseThrow { NoSuchElementException("Category with id ${req.categoryId} not found") } else current.category

        val updated = Step(
            id = current.id,
            category = category,
            name = req.name ?: current.name,
            description = req.description ?: current.description,
            isActive = req.isActive ?: current.isActive,
            stepNumber = req.stepNumber ?: current.stepNumber,
            orderSteps = current.orderSteps,
        )
        val saved = stepRepository.save(updated)
        return saved.toStepDetailResponse()
    }

    override fun delete(id: Long) {
        val step = stepRepository.findById(id).orElseThrow { NoSuchElementException("Step with id $id not found") }
        stepRepository.delete(step)
    }

    @Transactional
    override fun changeOrder(id: Long, req: StepOrderRequest): StepResponse {
        val step = stepRepository.findById(id).orElseThrow { NoSuchElementException("Step with id $id not found") }
        val oldPos = step.stepNumber
        val newPos = req.stepNumber
        if (newPos == oldPos) return step.toStepResponse()

        // Re-sequence other steps
        val all = stepRepository.findAll()
        val updated = mutableListOf<Step>()
        if (newPos < oldPos) {
            all.filter { it.stepNumber in newPos until oldPos }.forEach { s ->
                updated.add(
                    Step(
                        id = s.id,
                        category = s.category,
                        name = s.name,
                        description = s.description,
                        isActive = s.isActive,
                        stepNumber = s.stepNumber + 1,
                        orderSteps = s.orderSteps,
                    )
                )
            }
        } else {
            all.filter { it.stepNumber in (oldPos + 1)..newPos }.forEach { s ->
                updated.add(
                    Step(
                        id = s.id,
                        category = s.category,
                        name = s.name,
                        description = s.description,
                        isActive = s.isActive,
                        stepNumber = s.stepNumber - 1,
                        orderSteps = s.orderSteps,
                    )
                )
            }
        }

        // Replace current
        val moved = Step(
            id = step.id,
            category = step.category,
            name = step.name,
            description = step.description,
            isActive = step.isActive,
            stepNumber = newPos,
            orderSteps = step.orderSteps,
        )

        stepRepository.saveAll(updated)
        val saved = stepRepository.save(moved)
        return saved.toStepResponse()
    }
}

