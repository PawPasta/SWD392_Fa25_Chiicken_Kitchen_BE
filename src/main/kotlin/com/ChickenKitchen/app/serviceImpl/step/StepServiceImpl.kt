package com.ChickenKitchen.app.serviceImpl.step

import com.ChickenKitchen.app.handler.CategoryNotFoundException
import com.ChickenKitchen.app.handler.StepHasOrderStepsException
import com.ChickenKitchen.app.handler.StepNameExistInCategoryException
import com.ChickenKitchen.app.handler.StepNotFoundException
import com.ChickenKitchen.app.handler.StepNumberConflictException
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
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import kotlin.math.max

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

    override fun getAll(pageNumber: Int, size: Int): List<StepResponse>? {
        val safeSize = max(size, 1)
        val safePage = max(pageNumber, 1)
        val pageable = PageRequest.of(safePage - 1, safeSize, Sort.by("stepNumber").ascending())
        val page = stepRepository.findAll(pageable)
        if (page.isEmpty) return null
        return page.content.toStepResponseList()
    }

    override fun getById(id: Long): StepDetailResponse {
        val step = stepRepository.findById(id)
            .orElseThrow { StepNotFoundException("Step with id $id not found") }
        return step.toStepDetailResponse()
    }

    override fun create(req: CreateStepRequest): StepDetailResponse {
        val category = categoryRepository.findById(req.categoryId)
            .orElseThrow { CategoryNotFoundException("Category with id ${req.categoryId} not found") }

        // Kiểm tra trùng tên Step trong cùng Category
        val existingByName = stepRepository.findByCategoryIdAndNameIgnoreCase(req.categoryId, req.name)
        if (existingByName != null) {
            throw StepNameExistInCategoryException("Step name '${req.name}' already exists in this category")
        }

        // Kiểm tra trùng stepNumber trong cùng Category
        val existingByNumber = stepRepository.findByCategoryIdAndStepNumber(req.categoryId, req.stepNumber)
        if (existingByNumber != null) {
            throw StepNumberConflictException("Step number ${req.stepNumber} already exists in this category")
        }

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
        val current = stepRepository.findById(id)
            .orElseThrow { StepNotFoundException("Step with id $id not found") }

        val category = if (req.categoryId != null) {
            categoryRepository.findById(req.categoryId)
                .orElseThrow { CategoryNotFoundException("Category with id ${req.categoryId} not found") }
        } else current.category

        req.name?.let {
            val exist = stepRepository.findByCategoryIdAndNameIgnoreCase(category.id!!, it)
            if (exist != null && exist.id != current.id) {
                throw StepNameExistInCategoryException("Step name '$it' already exists in this category")
            }
        }

        // Nếu đổi stepNumber, kiểm tra trùng
        req.stepNumber?.let {
            val existNum = stepRepository.findByCategoryIdAndStepNumber(category.id!!, it)
            if (existNum != null && existNum.id != current.id) {
                throw StepNumberConflictException("Step number $it already exists in this category")
            }
        }

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
        val step = stepRepository.findById(id)
            .orElseThrow { StepNotFoundException("Step with id $id not found") }

        if (step.orderSteps.isNotEmpty()) {
            throw StepHasOrderStepsException("Cannot delete step because it is used in order steps")
        }

        stepRepository.delete(step)
    }

    override fun count(): Long = stepRepository.count()

    @Transactional
    override fun changeOrder(id: Long, req: StepOrderRequest): StepResponse {
        val step = stepRepository.findById(id)
            .orElseThrow { StepNotFoundException("Step with id $id not found") }
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
