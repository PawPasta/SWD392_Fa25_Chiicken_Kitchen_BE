package com.ChickenKitchen.app.model.entity.menu

import com.ChickenKitchen.app.model.entity.ingredient.Store
import jakarta.persistence.*
import java.sql.Timestamp


@Entity
@Table(
    name = "daily_menu",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["store_id", "menu_item_id", "menu_date"])
    ]
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

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,
)