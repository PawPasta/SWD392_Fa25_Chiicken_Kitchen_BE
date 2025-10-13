package com.ChickenKitchen.app.model.entity.ingredient

import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import com.ChickenKitchen.app.model.entity.order.Order
import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "stores")
class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val address: String,

    @Column(nullable = false, length = 100)
    val phone: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @OneToMany(mappedBy = "store")
    val orders: MutableList<Order> = mutableListOf(),

    @OneToMany(mappedBy = "store")
    val dailyMenus: MutableList<DailyMenu> = mutableListOf(),

    @OneToMany(mappedBy = "store")
    val ingredientBatches: MutableList<StoreIngredientBatch> = mutableListOf()
)