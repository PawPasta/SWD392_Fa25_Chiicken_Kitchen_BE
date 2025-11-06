package com.ChickenKitchen.app.serviceImpl.ingredient

import com.ChickenKitchen.app.handler.StoreAddressExistException
import com.ChickenKitchen.app.handler.StoreHasIngredientsException
import com.ChickenKitchen.app.handler.StoreHasOrdersException
import com.ChickenKitchen.app.handler.StoreNameExistException
import com.ChickenKitchen.app.handler.StoreNotFoundException
import com.ChickenKitchen.app.mapper.toListStoreResponse
import com.ChickenKitchen.app.mapper.toStoreResponse
import com.ChickenKitchen.app.model.dto.request.CreateStoreRequest
import com.ChickenKitchen.app.model.dto.request.UpdateStoreRequest
import com.ChickenKitchen.app.model.dto.response.StoreResponse
import com.ChickenKitchen.app.model.entity.ingredient.Store
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.service.ingredient.StoreService
import org.springframework.stereotype.Service
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import kotlin.math.max
import jakarta.persistence.criteria.Predicate


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

    override fun getAll(pageNumber: Int, size: Int): List<StoreResponse>? {
        val safeSize = max(size, 1)
        val safePage = max(pageNumber, 1)
        val pageable = PageRequest.of(safePage - 1, safeSize)
        val page = storeRepository.findAll(pageable)
        if (page.isEmpty) return null
        return page.content.toListStoreResponse()
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

        // Daily menu feature removed: no menu linkage check

        if (store.ingredientBatches.isNotEmpty()) {
            throw StoreHasIngredientsException("Cannot delete Store with id $id: it has ${store.ingredientBatches.size} ingredient batches")
        }

        storeRepository.delete(store)
    }

    override fun count(): Long = storeRepository.count()
    override fun search(name: String?, city: String?, sortBy: String, direction: String): List<Store> {
        val spec = Specification<Store> { root, query, cb ->
            val preds = mutableListOf<Predicate>()
            if (!name.isNullOrBlank()) {
                preds.add(cb.like(cb.lower(root.get("name")), "%${name.trim().lowercase()}%"))
            }
            if (!city.isNullOrBlank()) {
                preds.add(cb.equal(cb.lower(root.get("city")), city.trim().lowercase()))
            }
            if (preds.isNotEmpty()) {
                query?.where(*preds.toTypedArray()) // ✅ safe call
            }
            null
        }

        val sort = if (direction.equals("desc", true)) Sort.by(sortBy).descending() else Sort.by(sortBy).ascending()
        return storeRepository.findAll(spec, sort)
    }
}
