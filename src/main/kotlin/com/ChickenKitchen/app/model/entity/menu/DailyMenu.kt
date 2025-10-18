package com.ChickenKitchen.app.model.entity.menu

import com.ChickenKitchen.app.model.entity.ingredient.Store
import jakarta.persistence.*
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp


@Entity
@Table(
    name = "daily_menus",
    uniqueConstraints = [UniqueConstraint(columnNames = ["store_id", "menu_date"])]
)
class DailyMenu(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "menu_date", nullable = false)
    var menuDate: Timestamp,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: Timestamp? = null,

    // Daily menu có nhiều món
    @OneToMany(mappedBy = "dailyMenu", cascade = [CascadeType.ALL], orphanRemoval = true)
    val dailyMenuItems: MutableList<DailyMenuItem> = mutableListOf(),

    // Daily menu áp dụng cho nhiều store
    @ManyToMany
    @JoinTable(
        name = "daily_menu_stores",
        joinColumns = [JoinColumn(name = "daily_menu_id")],
        inverseJoinColumns = [JoinColumn(name = "store_id")]
    )
    var stores: MutableSet<Store> = mutableSetOf()
)