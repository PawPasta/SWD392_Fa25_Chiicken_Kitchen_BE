package com.ChickenKitchen.app.serviceImpl.promotion

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

    // cái này cho người dùng nè
    override fun getAllByActive(): List<PromotionResponse>? {
        val list = promotionRepository.findAllByIsActive(true)
        if(list.isEmpty()) return null
        return  list.toPromotionList()
    }

    override fun changeStatus(id: Long): PromotionResponse {
        val promotion = promotionRepository.findById(id).orElseThrow { NoSuchElementException ("Cannot find promotion with $id") }
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
        val getById = promotionRepository.findById(id).orElseThrow { NoSuchElementException ("Cannot find promotion with $id") }
        return getById.toPromotionDetailResponse()
    }

    override fun create(req: CreatePromotionRequest): PromotionDetailResponse {
       val promotion = Promotion(
           discountType = req.discountType,
           discountValue = req.discountValue,
           startDate = req.startDate,
           endDate = req.endDate
       )

        val save = promotionRepository.save(promotion)

        return save.toPromotionDetailResponse()
    }

    override fun update(
        id: Long,
        req: UpdatePromotionRequest
    ): PromotionDetailResponse {
        val currentPromotion = promotionRepository.findById(id).orElseThrow { NoSuchElementException ("Cannot find promotion with $id") }

        val update = Promotion (
            discountType = currentPromotion.discountType,
            discountValue = req.discountValue ?: currentPromotion.discountValue,
            startDate = currentPromotion.startDate,
            endDate = req.endDate ?: currentPromotion.endDate,
        )

        val saved = promotionRepository.save(update)

        return saved.toPromotionDetailResponse()

    }

    // Cai nay chac chi co the xoa duoc khi OrderPromotion dang null
    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}