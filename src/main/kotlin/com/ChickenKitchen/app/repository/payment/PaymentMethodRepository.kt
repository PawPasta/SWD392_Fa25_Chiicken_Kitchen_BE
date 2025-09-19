package com.ChickenKitchen.app.repository.payment

import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.enum.PaymentMethodType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PaymentMethodRepository : JpaRepository<PaymentMethod, Long> {
    fun findByName(name: PaymentMethodType): Optional<PaymentMethod>
    fun findAllByIsActive(isActive: Boolean = true): List<PaymentMethod>
}
