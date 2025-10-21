package com.ChickenKitchen.app.serviceImpl.menu

import com.ChickenKitchen.app.handler.DailyMenuAlreadyExistsException
import com.ChickenKitchen.app.handler.DailyMenuHasStoresException
import com.ChickenKitchen.app.handler.DailyMenuNotFoundException
import com.ChickenKitchen.app.handler.StoreNotFoundException
import com.ChickenKitchen.app.mapper.toDailyMenuDetailResponse
import com.ChickenKitchen.app.mapper.toDailyMenuListResponse
import com.ChickenKitchen.app.model.dto.request.CreateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.request.UpdateDailyMenuRequest
import com.ChickenKitchen.app.model.dto.response.DailyMenuResponse
import com.ChickenKitchen.app.model.dto.response.DailyMenuByStoreResponse
import com.ChickenKitchen.app.model.dto.response.DailyMenuCategoryGroupResponse
import com.ChickenKitchen.app.model.dto.response.DailyMenuDetailResponse
import com.ChickenKitchen.app.model.dto.response.MenuItemResponse
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

    override fun getById(id: Long): DailyMenuDetailResponse {
        val dailyMenu = dailyMenuRepository.findById(id)
            .orElseThrow { DailyMenuNotFoundException("Cannot find daily menu with id $id") }
        return dailyMenu.toDailyMenuDetailResponse()
    }

    override fun create(req: CreateDailyMenuRequest): DailyMenuDetailResponse {
        if (dailyMenuRepository.existsByMenuDate(req.menuDate)) {
            throw DailyMenuAlreadyExistsException("Daily menu for date ${req.menuDate} already exists")
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

        return savedMenu.toDailyMenuDetailResponse()
    }

    override fun update(
        id: Long,
        req: UpdateDailyMenuRequest
    ): DailyMenuDetailResponse {
        val dailyMenu = dailyMenuRepository.findById(id)
            .orElseThrow { DailyMenuNotFoundException("Cannot find daily menu with id $id") }

        req.menuDate?.let { newDate ->
            if (dailyMenu.menuDate != newDate && dailyMenuRepository.existsByMenuDate(newDate)) {
                throw DailyMenuAlreadyExistsException("Daily menu for date $newDate already exists")
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

        return updatedMenu.toDailyMenuDetailResponse()
    }

    override fun delete(id: Long) {
        val dailyMenu = dailyMenuRepository.findById(id)
            .orElseThrow { DailyMenuNotFoundException("Cannot find daily menu with id $id") }

        // Kiểm tra nếu vẫn còn liên kết store
        if (dailyMenu.stores.isNotEmpty()) {
            throw DailyMenuHasStoresException(
                "Cannot delete daily menu with id $id: it is associated with ${dailyMenu.stores.size} stores"
            )
        }

        dailyMenuRepository.delete(dailyMenu)
    }

    override fun getByStoreAndDate(storeId: Long, date: String): DailyMenuByStoreResponse {
        // Expect date as yyyy-MM-dd (local date). Build start/end range in UTC+0; DB side uses Timestamp
        val start = java.sql.Timestamp.valueOf("${date} 00:00:00")
        val end = java.sql.Timestamp.valueOf("${date} 23:59:59")

        val dailyMenu = dailyMenuRepository.findByStoreAndDateRange(storeId, start, end)
            ?: throw DailyMenuNotFoundException("No daily menu for store $storeId on $date")

        val store = dailyMenu.stores.firstOrNull { it.id == storeId }
            ?: throw StoreNotFoundException("Store $storeId not linked to menu ${dailyMenu.id}")

        // Group items by category
        val items = dailyMenu.dailyMenuItems.map { dmi ->
            val mi = dmi.menuItem
            MenuItemResponse(
                id = mi.id!!,
                name = mi.name,
                categoryId = mi.category.id!!,
                categoryName = mi.category.name,
                isActive = mi.isActive,
                imageUrl = mi.imageUrl,
                price = mi.price,
                cal = mi.cal,
                description = mi.description,
            )
        }
        val grouped = items.groupBy { it.categoryId }
            .map { (catId, list) ->
                DailyMenuCategoryGroupResponse(
                    categoryId = catId,
                    categoryName = list.first().categoryName,
                    items = list
                )
            }
            .sortedBy { it.categoryName }

        return DailyMenuByStoreResponse(
            storeId = store.id!!,
            storeName = store.name,
            menuDate = date,
            categories = grouped
        )
    }
}
