package com.ChickenKitchen.app.model.entity.menu

import com.ChickenKitchen.app.model.entity.ingredient.Store
import jakarta.persistence.*
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp


@Entity
@Table(
    name = "daily_menu",
)
class DailyMenu(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    val menuItem: MenuItem,

    @Column(name = "menu_date", nullable = false)
    val menuDate: Timestamp? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,
)