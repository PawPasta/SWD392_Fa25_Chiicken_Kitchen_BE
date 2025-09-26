package com.ChickenKitchen.app.model.entity.menu

import com.ChickenKitchen.app.enum.MenuType
import jakarta.persistence.*
import java.math.BigDecimal
import com.ChickenKitchen.app.model.entity.order.OrderItem

@Entity
@Table(name = "daily_menu_items")
class DailyMenuItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_menu_id", nullable = false)
    var dailyMenu: DailyMenu,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var menuType: MenuType,

    @Column(nullable = false)
    var refId: Long,  // id of the referenced item 

    @Column(nullable = false)
    var cal: Int,

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO,

    @OneToOne(mappedBy = "dailyMenuItem", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var orderItem: OrderItem? = null
)