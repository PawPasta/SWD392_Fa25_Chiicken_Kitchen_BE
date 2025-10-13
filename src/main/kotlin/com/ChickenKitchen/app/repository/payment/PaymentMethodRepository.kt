package com.ChickenKitchen.app.repository.payment

import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PaymentMethodRepository : JpaRepository<PaymentMethod, Long> {
}