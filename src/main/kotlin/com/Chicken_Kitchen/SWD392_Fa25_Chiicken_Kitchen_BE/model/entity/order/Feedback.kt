package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.order

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.user.User
import jakarta.persistence.*
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "feedbacks")
class Feedback(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Lob
    var feedback: String? = null,

    var rating: Int? = null,

    @Lob
    var reply: String? = null,

    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: Timestamp,

    var repliedAt: Timestamp? = null
)
