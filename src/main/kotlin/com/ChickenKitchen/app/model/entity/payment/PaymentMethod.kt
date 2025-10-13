package com.ChickenKitchen.app.model.entity.payment

import jakarta.persistence.*

@Entity
@Table(name = "payment_methods")
class PaymentMethod(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column
    val description: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "paymentMethod")
    val transactions: MutableList<Transaction> = mutableListOf()
)