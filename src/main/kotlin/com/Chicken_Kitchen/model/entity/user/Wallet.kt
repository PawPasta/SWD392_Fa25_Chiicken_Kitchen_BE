package com.Chicken_Kitchen.model.entity.user
import jakarta.persistence.*
import java.sql.Timestamp
import java.math.BigDecimal
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.CreationTimestamp
import com.Chicken_Kitchen.model.entity.user.User

@Entity 
@Table(name = "wallet")
class Wallet(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    var user: User,

    @Column(nullable = false, precision = 12, scale = 2)
    var balance: BigDecimal = BigDecimal.ZERO,

    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: Timestamp? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Timestamp? = null,
)