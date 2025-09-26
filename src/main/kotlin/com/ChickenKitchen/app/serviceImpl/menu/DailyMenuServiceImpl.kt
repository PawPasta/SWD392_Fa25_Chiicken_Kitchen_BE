package com.ChickenKitchen.app.serviceImpl.menu

import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.mapper.toDailyMenuDetailResponse
import com.ChickenKitchen.app.mapper.toDailyMenuResponse
import com.ChickenKitchen.app.mapper.toDailyMenuResponseList
import com.ChickenKitchen.app.model.dto.request.CreateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.request.DailyMenuItemRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.response.DailyMenuDetailResponse
import com.ChickenKitchen.app.model.dto.response.DailyMenuResponse
import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import com.ChickenKitchen.app.model.entity.menu.DailyMenuItem
import com.ChickenKitchen.app.repository.menu.DailyMenuItemRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuRepository
import com.ChickenKitchen.app.service.menu.DailyMenuService
import org.springframework.stereotype.Service

@Service
class DailyMenuServiceImpl(
    private val dailyMenuRepository: DailyMenuRepository,
    private val dailyMenuItemRepository: DailyMenuItemRepository
) : DailyMenuService {

    override fun getAll(): List<DailyMenuResponse>? {
        val list = dailyMenuRepository.findAll()
        if (list.isEmpty()) return null
        return list.toDailyMenuResponseList()
    }

    override fun getById(id: Long): DailyMenuDetailResponse {
        val dm = dailyMenuRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("DailyMenu with id $id not found")
        return dm.toDailyMenuDetailResponse()
    }

    override fun create(req: CreateDailyMenuRequest): DailyMenuDetailResponse {
        val dm = dailyMenuRepository.save(
            DailyMenu(
                date = req.date,
                name = req.name
            )
        )

        applyItems(dm, req.items)
        val saved = dailyMenuRepository.save(dm)
        return saved.toDailyMenuDetailResponse()
    }

    override fun update(id: Long, req: UpdateDailyMenuRequest): DailyMenuDetailResponse {
        val dm = dailyMenuRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("DailyMenu with id $id not found")

        req.date?.let { dm.date = it }
        req.name?.let { dm.name = it }
        req.items?.let { applyItems(dm, it) }

        val saved = dailyMenuRepository.save(dm)
        return saved.toDailyMenuDetailResponse()
    }

    override fun delete(id: Long) {
        val dm = dailyMenuRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("DailyMenu with id $id not found")
        dailyMenuRepository.delete(dm)
    }

    private fun applyItems(dm: DailyMenu, items: List<DailyMenuItemRequest>?) {
        if (items.isNullOrEmpty()) {
            dm.dailyMenuItems.clear()
            return
        }

        val newItems = items.map { reqItem ->
            DailyMenuItem(
                dailyMenu = dm,
                name = reqItem.name,
                menuType = reqItem.menuType,
                refId = reqItem.refId,
                cal = reqItem.cal,
                price = reqItem.price
            )
        }

        dm.dailyMenuItems.apply {
            clear()
            addAll(newItems)
        }
    }
}
