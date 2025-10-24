package com.ChickenKitchen.app.model.entity.user

import com.ChickenKitchen.app.model.entity.ingredient.Store
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
@Table(
    name = "employee_details",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id"]) // one User has only one employee detail
    ],
    indexes = [
        Index(name = "idx_employee_store_id", columnList = "store_id")
    ]
)
class EmployeeDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,

    @Column(name = "position", length = 100)
    var position: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: Timestamp? = null,

    @Column(columnDefinition = "TEXT")
    var note: String? = null,
)

