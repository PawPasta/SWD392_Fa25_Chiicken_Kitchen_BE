package com.Chicken_Kitchen.repository.payment

import com.Chicken_Kitchen.model.entity.payment.PaymentMethod
import com.Chicken_Kitchen.enum.PaymentMethodType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PaymentMethodRepository : JpaRepository<PaymentMethod, Long> {
    fun findByName(name: PaymentMethodType): Optional<PaymentMethod>
    fun findAllByIsActive(isActive: Boolean = true): List<PaymentMethod>
}
