package com.ChickenKitchen.app.serviceImpl.menu

import com.ChickenKitchen.app.mapper.toDailyMenuListResponse
import com.ChickenKitchen.app.mapper.toDailyMenuResponse
import com.ChickenKitchen.app.model.dto.request.CreateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.response.DailyMenuResponse
import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import com.ChickenKitchen.app.model.entity.menu.DailyMenuItem
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuRepository
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.service.menu.DailyMenuService
import org.springframework.stereotype.Service


@Service
class DailyMenuServiceImpl(

    private val dailyMenuRepository: DailyMenuRepository,
    private val storeRepository: StoreRepository,
    private val menuItemRepository: MenuItemRepository

) : DailyMenuService{
    override fun getAll(): List<DailyMenuResponse>? {
        val list = dailyMenuRepository.findAll()
        if (list.isEmpty()) return null
        return list.toDailyMenuListResponse()
    }

    override fun getById(id: Long): DailyMenuResponse {
        val dailyMenu = dailyMenuRepository.findById(id).orElseThrow { NoSuchElementException("Cannot find daily menu with that $id") }
        return dailyMenu.toDailyMenuResponse()
    }

    override fun create(req: CreateDailyMenuRequest): DailyMenuResponse {
        if (dailyMenuRepository.existsByMenuDate(req.menuDate)) {
            throw IllegalArgumentException("Daily menu for date ${req.menuDate} already exists")
        }

        val allStores = storeRepository.findAll().toMutableSet()

        val dailyMenu = DailyMenu(
            menuDate = req.menuDate,
            stores = allStores
        )

        val menuItems = menuItemRepository.findAllById(req.menuItemIds)
        menuItems.forEach { menuItem ->
            val dailyMenuItem = DailyMenuItem(
                dailyMenu = dailyMenu,
                menuItem = menuItem
            )
            dailyMenu.dailyMenuItems.add(dailyMenuItem)
        }

        val savedMenu = dailyMenuRepository.save(dailyMenu)

        return savedMenu.toDailyMenuResponse()
    }

    override fun update(
        id: Long,
        req: UpdateDailyMenuRequest
    ): DailyMenuResponse {
        val dailyMenu = dailyMenuRepository.findById(id)
            .orElseThrow { NoSuchElementException("Cannot find daily menu with id $id") }

        req.menuDate?.let { newDate ->
            if (dailyMenu.menuDate != newDate && dailyMenuRepository.existsByMenuDate(newDate)) {
                throw IllegalArgumentException("Daily menu for date $newDate already exists")
            }
            dailyMenu.menuDate = newDate
        }


        req.menuItemIds?.let { ids ->

            dailyMenu.dailyMenuItems.clear()

            val menuItems = menuItemRepository.findAllById(ids)
            menuItems.forEach { menuItem ->
                val dailyMenuItem = DailyMenuItem(
                    dailyMenu = dailyMenu,
                    menuItem = menuItem
                )
                dailyMenu.dailyMenuItems.add(dailyMenuItem)
            }
        }


        req.storeIds?.let { ids ->
            val stores = storeRepository.findAllById(ids).toMutableSet()
            dailyMenu.stores.clear()
            dailyMenu.stores.addAll(stores)
        }


        val updatedMenu = dailyMenuRepository.save(dailyMenu)

        return updatedMenu.toDailyMenuResponse()
    }


    // Cái này thì khoen đã, không nghĩ là có thể đặt được
    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}