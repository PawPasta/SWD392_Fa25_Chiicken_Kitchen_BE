package com.ChickenKitchen.app.serviceImpl.ingredient

import com.ChickenKitchen.app.handler.StoreAddressExistException
import com.ChickenKitchen.app.handler.StoreHasIngredientsException
import com.ChickenKitchen.app.handler.StoreHasOrdersException
import com.ChickenKitchen.app.handler.StoreNameExistException
import com.ChickenKitchen.app.handler.StoreNotFoundException
import com.ChickenKitchen.app.handler.StoreUsedInMenuException
import com.ChickenKitchen.app.mapper.toListStoreResponse
import com.ChickenKitchen.app.mapper.toStoreResponse
import com.ChickenKitchen.app.model.dto.request.CreateStoreRequest
import com.ChickenKitchen.app.model.dto.request.UpdateStoreRequest
import com.ChickenKitchen.app.model.dto.response.StoreResponse
import com.ChickenKitchen.app.model.entity.ingredient.Store
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.service.ingredient.StoreService
import org.springframework.stereotype.Service


@Service
class StoreServiceImpl (
    private val storeRepository: StoreRepository
): StoreService{

    override fun changeStatus(id: Long): StoreResponse {
        val store = storeRepository.findById(id)
            .orElseThrow { StoreNotFoundException("Cannot find Store with id $id") }
        store.isActive = !store.isActive
        val saved = storeRepository.save(store)
        return saved.toStoreResponse()
    }

    override fun getAll(): List<StoreResponse>? {
        val list = storeRepository.findAll()
        if (list.isEmpty()) return null
        return list.toListStoreResponse()
    }

    override fun getById(id: Long): StoreResponse {
        val store = storeRepository.findById(id)
            .orElseThrow { StoreNotFoundException("Cannot find Store with id $id") }
        return store.toStoreResponse()
    }

    override fun create(req: CreateStoreRequest): StoreResponse {

        val existingByName = storeRepository.findByName(req.name)
        if (existingByName != null) {
            throw StoreNameExistException("Store name '${req.name}' already exists")
        }

        val existingByAddress = storeRepository.findByAddress(req.address)
        if (existingByAddress != null) {
            throw StoreAddressExistException("Store address '${req.address}' already exists")
        }


        val new = Store (
            address = req.address,
            name = req.name,
            phone = req.phone
        )
        val saved = storeRepository.save(new)
        return saved.toStoreResponse()
    }

    override fun update(
        id: Long,
        req: UpdateStoreRequest
    ): StoreResponse {
        val store = storeRepository.findById(id).orElseThrow { StoreNotFoundException("Cannot find Store with id $id") }

        val update  = Store(
            address = req.address ?: store.address,
            name = req.name ?: store.name,
            phone = req.phone ?: store.phone,
        )

        val saved = storeRepository.save(update)

        return saved.toStoreResponse()
    }

    override fun delete(id: Long) {
        val store = storeRepository.findById(id)
            .orElseThrow { StoreNotFoundException("Cannot find Store with id $id") }

        if (store.orders.isNotEmpty()) {
            throw StoreHasOrdersException("Cannot delete Store with id $id: it has ${store.orders.size} orders")
        }

        if (store.dailyMenus.isNotEmpty()) {
            throw StoreUsedInMenuException("Cannot delete Store with id $id: it is used in ${store.dailyMenus.size} daily menus")
        }

        if (store.ingredientBatches.isNotEmpty()) {
            throw StoreHasIngredientsException("Cannot delete Store with id $id: it has ${store.ingredientBatches.size} ingredient batches")
        }

        storeRepository.delete(store)
    }
}