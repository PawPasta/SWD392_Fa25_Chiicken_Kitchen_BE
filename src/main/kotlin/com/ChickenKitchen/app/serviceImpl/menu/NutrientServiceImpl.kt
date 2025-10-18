package com.ChickenKitchen.app.serviceImpl.menu

import com.ChickenKitchen.app.handler.NutrientHasMenuItemsException
import com.ChickenKitchen.app.handler.NutrientNameExistException
import com.ChickenKitchen.app.handler.NutrientNotFoundException
import com.ChickenKitchen.app.model.dto.request.CreateNutrientRequest
import com.ChickenKitchen.app.model.dto.request.UpdateNutrientRequest
import com.ChickenKitchen.app.model.dto.response.NutrientDetailResponse
import com.ChickenKitchen.app.model.dto.response.NutrientResponse
import com.ChickenKitchen.app.model.entity.menu.Nutrient
import com.ChickenKitchen.app.repository.menu.NutrientRepository
import com.ChickenKitchen.app.service.menu.NutrientService
import com.ChickenKitchen.app.mapper.toNutrientDetailResponse
import com.ChickenKitchen.app.mapper.toNutrientResponse
import com.ChickenKitchen.app.mapper.toNutrientResponseList
import org.springframework.stereotype.Service

@Service
class NutrientServiceImpl(
    private val nutrientRepository: NutrientRepository
) : NutrientService {

    override fun getAll(): List<NutrientResponse>? {
        val list = nutrientRepository.findAll()
        if (list.isEmpty()) return null
        return list.toNutrientResponseList()
    }

    override fun getById(id: Long): NutrientDetailResponse {
        val entity = nutrientRepository.findById(id)
            .orElseThrow { NutrientNotFoundException("Nutrient with id $id not found") }
        return entity.toNutrientDetailResponse()
    }

    override fun create(req: CreateNutrientRequest): NutrientDetailResponse {
        val existedByName = nutrientRepository.findByName(req.name)
        if(existedByName != null){
            throw NutrientNameExistException("Nutrient Name is already exists")
        }
        val entity = Nutrient(
            name = req.name,
            baseUnit = req.baseUnit,
        )
        val saved = nutrientRepository.save(entity)
        return saved.toNutrientDetailResponse()
    }

    override fun update(id: Long, req: UpdateNutrientRequest): NutrientDetailResponse {
        val current = nutrientRepository.findById(id)
            .orElseThrow { NutrientNotFoundException("Nutrient with id $id not found") }
        val updated = Nutrient(
            id = current.id,
            name = req.name ?: current.name,
            baseUnit = req.baseUnit ?: current.baseUnit,
            menuItemNutrients = current.menuItemNutrients
        )
        val saved = nutrientRepository.save(updated)
        return saved.toNutrientDetailResponse()
    }

    override fun delete(id: Long) {
        val current = nutrientRepository.findById(id)
            .orElseThrow { NutrientNotFoundException("Nutrient with id $id not found") }


        if (current.menuItemNutrients.isNotEmpty()) {
            throw NutrientHasMenuItemsException("Cannot delete Nutrient with id $id: it is used in ${current.menuItemNutrients.size} menu items")
        }

        nutrientRepository.delete(current)
    }
}

