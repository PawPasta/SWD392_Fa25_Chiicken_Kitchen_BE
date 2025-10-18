package com.ChickenKitchen.app.model.entity.payment

import jakarta.persistence.*

@Entity
@Table(name = "payment_methods")
class PaymentMethod(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column
    var description: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "paymentMethod")
    val transactions: MutableList<Transaction> = mutableListOf()
)