package com.ChickenKitchen.app.serviceImpl.promotion

import com.ChickenKitchen.app.handler.PromotionHasOrdersException
import com.ChickenKitchen.app.handler.PromotionNotFoundException
import com.ChickenKitchen.app.mapper.toPromotionDetailResponse
import com.ChickenKitchen.app.mapper.toPromotionList
import com.ChickenKitchen.app.mapper.toPromotionResponse
import com.ChickenKitchen.app.model.dto.request.CreatePromotionRequest
import com.ChickenKitchen.app.model.dto.request.UpdatePromotionRequest
import com.ChickenKitchen.app.model.dto.response.PromotionDetailResponse
import com.ChickenKitchen.app.model.dto.response.PromotionResponse
import com.ChickenKitchen.app.model.entity.promotion.Promotion
import com.ChickenKitchen.app.repository.promotion.PromotionRepository
import com.ChickenKitchen.app.service.promotion.PromotionService
import org.springframework.stereotype.Service


@Service
class PromotionServiceImpl (
    private val promotionRepository: PromotionRepository
): PromotionService{

    override fun changeStatus(id: Long): PromotionResponse {
        val promotion = promotionRepository.findById(id)
            .orElseThrow { PromotionNotFoundException("Cannot find promotion with id $id") }
        promotion.isActive = !promotion.isActive
        val updated = promotionRepository.save(promotion)
        return updated.toPromotionResponse()
    }

    override fun getAll(): List<PromotionResponse>? {
       val list = promotionRepository.findAll()
        if(list.isEmpty()) return null
        return  list.toPromotionList()
    }

    override fun getById(id: Long): PromotionDetailResponse {
        val promotion = promotionRepository.findById(id)
            .orElseThrow { PromotionNotFoundException("Cannot find promotion with id $id") }
        return promotion.toPromotionDetailResponse()
    }

    override fun create(req: CreatePromotionRequest): PromotionDetailResponse {

       val promotion = Promotion(
           name = req.name,
           description = req.description,
           code = req.code,
           discountType = req.discountType,
           discountValue = req.discountValue,
           startDate = req.startDate,
           endDate = req.endDate
       )

        val save = promotionRepository.save(promotion)

        return save.toPromotionDetailResponse()
    }

    override fun update(id: Long, req: UpdatePromotionRequest): PromotionDetailResponse {
        val promotion = promotionRepository.findById(id)
            .orElseThrow { PromotionNotFoundException("Cannot find promotion with id $id") }

        promotion.name = req.name ?: promotion.name
        promotion.description = req.description ?: promotion.description
        promotion.code = req.code ?: promotion.code
        promotion.discountValue = req.discountValue ?: promotion.discountValue
        promotion.endDate = req.endDate ?: promotion.endDate
        promotion.isActive = req.isActive ?: promotion.isActive
        promotion.quantity = req.quantity ?: promotion.quantity

        val saved = promotionRepository.save(promotion)
        return saved.toPromotionDetailResponse()
    }

    override fun delete(id: Long) {
        val promotion = promotionRepository.findById(id)
            .orElseThrow { PromotionNotFoundException("Cannot find promotion with id $id") }

        if (promotion.orderPromotions.isNotEmpty()) {
            throw PromotionHasOrdersException(
                "Cannot delete promotion with id $id: it is associated with ${promotion.orderPromotions.size} orders"
            )
        }

        promotionRepository.delete(promotion)
    }
}