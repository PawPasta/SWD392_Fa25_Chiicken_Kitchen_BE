package com.ChickenKitchen.app.model.entity.menu

import jakarta.persistence.*


@Entity
@Table(name = "daily_menu_items")
class DailyMenuItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_menu_id", nullable = false)
    val dailyMenu: DailyMenu,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    val menuItem: MenuItem
)