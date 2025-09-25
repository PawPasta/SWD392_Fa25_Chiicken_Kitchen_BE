package com.ChickenKitchen.app.serviceImpl.promotion

import com.ChickenKitchen.app.handler.IngredientNotFoundException
import com.ChickenKitchen.app.mapper.toPromotionDetailResponse
import com.ChickenKitchen.app.mapper.toPromotionResponse
import com.ChickenKitchen.app.mapper.toPromotionResponseList
import com.ChickenKitchen.app.model.dto.request.CreatePromotionRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePromotionRequest
import com.ChickenKitchen.app.model.dto.response.PromotionDetailResponse
import com.ChickenKitchen.app.model.dto.response.PromotionResponse
import com.ChickenKitchen.app.model.entity.promotion.Promotion
import com.ChickenKitchen.app.repository.promotion.PromotionRepository
import com.ChickenKitchen.app.service.promotion.PromotionService
import org.springframework.stereotype.Service

@Service
class PromotionServiceImpl(
    private val promotionRepository: PromotionRepository
) : PromotionService {

    override fun getAll(): List<PromotionResponse>? {
        val list = promotionRepository.findAll()
        if (list.isEmpty()) return null
        return list.toPromotionResponseList()
    }

    override fun getById(id: Long): PromotionDetailResponse {
        val promo = promotionRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Promotion with id $id not found")
        return promo.toPromotionDetailResponse()
    }

    override fun create(req: CreatePromotionRequest): PromotionDetailResponse {
        val newPromo = promotionRepository.save(
            Promotion(
                name = req.name,
                description = req.description,
                discountType = req.discountType,
                discountValue = req.discountValue,
                isActive = req.isActive,
                startDate = req.startDate,
                endDate = req.endDate,
                quantity = req.quantity
            )
        )
        return newPromo.toPromotionDetailResponse()
    }

    override fun update(id: Long, req: UpdatePromotionRequest): PromotionDetailResponse {
        val promo = promotionRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Promotion with id $id not found")

        req.name?.let { promo.name = it }
        req.description?.let { promo.description = it }
        req.discountType?.let { promo.discountType = it }
        req.discountValue?.let { promo.discountValue = it }
        req.startDate?.let { promo.startDate = it }
        req.endDate?.let { promo.endDate = it }
        req.quantity?.let { promo.quantity = it }
        req.isActive?.let { promo.isActive = it }

        val updated = promotionRepository.save(promo)
        return updated.toPromotionDetailResponse()
    }

    override fun delete(id: Long) {
        val promo = promotionRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Promotion with id $id not found")
        promotionRepository.delete(promo)
    }

    override fun changeStatus(id: Long): PromotionResponse {
        val promo = promotionRepository.findById(id).orElse(null)
            ?: throw IngredientNotFoundException("Promotion with id $id not found")
        promo.isActive = !promo.isActive
        val updated = promotionRepository.save(promo)
        return updated.toPromotionResponse()
    }
}
