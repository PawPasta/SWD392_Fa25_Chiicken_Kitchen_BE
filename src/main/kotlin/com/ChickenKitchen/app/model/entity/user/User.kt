package com.ChickenKitchen.app.model.entity.user

import com.ChickenKitchen.app.enum.Role
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate
import java.sql.Timestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.CreationTimestamp
import com.ChickenKitchen.app.model.entity.auth.UserSession

@Entity 
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.USER,

    @Column(nullable = false, unique = true)
    var fullname: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: Timestamp? = null,

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: Timestamp? = null,

    var isActive: Boolean = true,

    var firstName: String? = null,
    var lastName: String? = null,
    var birthday: LocalDate? = null,

    /** JSON sở thích / dị ứng… */
    @Column(columnDefinition = "json")
    var preferences: String? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var sessions: MutableList<UserSession> = mutableListOf(),
)