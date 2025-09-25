package com.ChickenKitchen.app.model.entity.menu

import jakarta.persistence.*
import java.sql.Date

@Entity
@Table(name = "daily_menus")
class DailyMenu(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var date: Date,  

    @Column(nullable = false)
    var name: String,

    @OneToMany(mappedBy = "dailyMenu", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var dailyMenuItems: MutableList<DailyMenuItem> = mutableListOf()
)