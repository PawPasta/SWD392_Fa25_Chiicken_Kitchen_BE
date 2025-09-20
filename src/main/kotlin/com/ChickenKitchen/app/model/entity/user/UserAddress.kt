package com.ChickenKitchen.app.model.entity.user
import jakarta.persistence.*
import java.time.Instant
import java.sql.Timestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.CreationTimestamp

@Entity 
@Table(name = "user_addresses")
class UserAddress(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    /** tên người nhận */
    @Column(nullable = false)
    var recipientName: String,

    @Column(nullable = false)
    var phone: String,

    @Column(nullable = false)
    var addressLine: String,

    @Column(nullable = false)
    var city: String,
    
    var isDefault: Boolean = false,

    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: Timestamp? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Timestamp? = null,

)
