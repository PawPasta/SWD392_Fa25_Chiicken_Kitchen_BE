package com.ChickenKitchen.app.model.entity.order

import jakarta.persistence.*
import java.time.LocalDate
import com.ChickenKitchen.app.enum.ItemType
import com.ChickenKitchen.app.model.entity.recipe.Recipe
import java.math.BigDecimal
import org.hibernate.annotations.CreationTimestamp
import com.ChickenKitchen.app.model.entity.menu.DailyMenuItem

@Entity
@Table(name = "order_items")
class OrderItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @OneToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "daily_menu_item_id", nullable = false)
    var dailyMenuItem: DailyMenuItem,

    @Column(nullable = false)
    var quantity: Int,

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var cal: Int,

    @Lob
    var note: String? = null
)
