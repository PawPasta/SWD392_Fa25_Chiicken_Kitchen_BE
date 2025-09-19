package com.ChickenKitchen.app.model.entity.payment

import jakarta.persistence.*
import com.ChickenKitchen.app.model.entity.transaction.Transaction
import com.ChickenKitchen.app.enum.PaymentMethodType

@Entity
@Table(name = "payment_methods")
class PaymentMethod(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: PaymentMethodType,

    var description: String? = null,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "paymentMethod", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var transactions: MutableList<Transaction> = mutableListOf(),
)
