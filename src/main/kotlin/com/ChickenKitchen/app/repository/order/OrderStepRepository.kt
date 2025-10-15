package com.ChickenKitchen.app.repository.order

import com.ChickenKitchen.app.model.entity.order.OrderStep
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderStepRepository : JpaRepository<OrderStep, Long>

