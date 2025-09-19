package com.Chicken_Kitchen.model.entity.transaction

import com.Chicken_Kitchen.model.entity.order.Order
import com.Chicken_Kitchen.model.entity.user.User
import com.Chicken_Kitchen.model.entity.payment.PaymentMethod
import jakarta.persistence.*
import java.math.BigDecimal
import java.sql.Timestamp
import com.Chicken_Kitchen.enum.TransactionType

@Entity
@Table(name = "transactions")
class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    var paymentMethod: PaymentMethod,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var transactionType: TransactionType,  // Enum: PAYMENT, REFUND...

    @Column(nullable = false, precision = 15, scale = 2)
    var amount: BigDecimal,

    @Column(nullable = false)
    var createdAt: Timestamp,

    @Lob
    var note: String? = null
)
