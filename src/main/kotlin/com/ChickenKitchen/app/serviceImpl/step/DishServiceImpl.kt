package com.ChickenKitchen.app.serviceImpl.step

import com.ChickenKitchen.app.handler.DeleteDishFailedException
import com.ChickenKitchen.app.handler.DishNotFoundException
import com.ChickenKitchen.app.mapper.toDishDetailResponse
import com.ChickenKitchen.app.mapper.toDishResponseList
import com.ChickenKitchen.app.model.dto.request.CreateDishBaseRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDishBaseRequest
import com.ChickenKitchen.app.model.dto.response.DishDetailResponse
import com.ChickenKitchen.app.model.dto.response.DishResponse
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.repository.order.OrderDishRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.service.step.DishService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.math.max

@Service
class DishServiceImpl(
    private val dishRepository: DishRepository,
    private val orderDishRepository: OrderDishRepository,
) : DishService {

    override fun getAll(): List<DishResponse>? {
        val list = dishRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
        if (list.isEmpty()) return null
        return list.toDishResponseList()
    }

    override fun getAll(pageNumber: Int, size: Int): List<DishResponse>? {
        val safeSize = max(size, 1)
        val safePage = max(pageNumber, 1)
        val pageable = PageRequest.of(safePage - 1, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"))
        val page = dishRepository.findAll(pageable)
        if (page.isEmpty) return null
        return page.content.toDishResponseList()
    }

    override fun getById(id: Long): DishDetailResponse {
        val dish = dishRepository.findById(id).orElseThrow { DishNotFoundException("Dish with id $id not found") }
        return dish.toDishDetailResponse()
    }

    override fun create(req: CreateDishBaseRequest): DishDetailResponse {
        val entity = Dish(
            price = req.price,
            cal = req.cal,
            isCustom = req.isCustom,
            note = req.note,
        )
        val saved = dishRepository.save(entity)
        return saved.toDishDetailResponse()
    }

    override fun update(id: Long, req: UpdateDishBaseRequest): DishDetailResponse {
        val current = dishRepository.findById(id).orElseThrow { DishNotFoundException("Dish with id $id not found") }
        val updated = Dish(
            id = current.id,
            price = req.price ?: current.price,
            cal = req.cal ?: current.cal,
            isCustom = req.isCustom ?: current.isCustom,
            note = req.note ?: current.note,
            createdAt = current.createdAt,
            updatedAt = current.updatedAt,
            orderSteps = current.orderSteps,
        )
        val saved = dishRepository.save(updated)
        return saved.toDishDetailResponse()
    }

    override fun delete(id: Long) {
        val current = dishRepository.findById(id).orElseThrow { DishNotFoundException("Dish with id $id not found") }
        val linkedOrderId = orderDishRepository.findOrderIdByDishId(current.id!!)
        if (linkedOrderId != null) {
            throw DeleteDishFailedException("Dish is linked to order $linkedOrderId; cannot delete")
        }
        dishRepository.delete(current)
    }

    override fun count(): Long = dishRepository.count()
}

