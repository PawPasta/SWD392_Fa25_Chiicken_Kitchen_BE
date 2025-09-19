package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.payment

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.payment.PaymentMethod
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.enum.PaymentMethodType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PaymentMethodRepository : JpaRepository<PaymentMethod, Long> {
    fun findByName(name: PaymentMethodType): Optional<PaymentMethod>
    fun findAllByIsActive(isActive: Boolean = true): List<PaymentMethod>
}
