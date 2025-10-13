package com.ChickenKitchen.app.repository.step

import com.ChickenKitchen.app.model.entity.step.StepItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface StepItemRepository : JpaRepository<StepItem, Long> {

}