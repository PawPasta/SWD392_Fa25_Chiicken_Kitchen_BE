package com.ChickenKitchen.app.serviceImpl.ingredient

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
        val store = storeRepository.findById(id).orElseThrow { NoSuchElementException("Cannot find Store with id $id") }
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
        val store = storeRepository.findById(id).orElseThrow { NoSuchElementException("Cannot find Store with id $id") }
        return store.toStoreResponse()
    }

    override fun create(req: CreateStoreRequest): StoreResponse {

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
        val store = storeRepository.findById(id).orElseThrow { NoSuchElementException("Cannot find Store with id $id") }

        val update  = Store(
            address = req.address ?: store.address,
            name = req.name ?: store.name,
            phone = req.phone ?: store.phone,
        )

        val saved = storeRepository.save(update)

        return saved.toStoreResponse()
    }

    // Cai nay de con dan do, khong biet store duoc phep xoa khong
    override fun delete(id: Long) {

    }
}