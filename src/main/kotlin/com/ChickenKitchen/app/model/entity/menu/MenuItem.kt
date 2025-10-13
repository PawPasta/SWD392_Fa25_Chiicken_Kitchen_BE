package com.ChickenKitchen.app.model.entity.menu

import com.ChickenKitchen.app.enum.MenuCategory
import com.ChickenKitchen.app.model.entity.ingredient.Recipe
import com.ChickenKitchen.app.model.entity.step.StepItem
import jakarta.persistence.*
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp


@Entity
@Table(name = "menu_items")
data class MenuItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: MenuCategory,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @OneToMany(mappedBy = "menuItem")
    val stepItems: MutableList<StepItem> = mutableListOf(),

    @OneToMany(mappedBy = "menuItem")
    val dailyMenus: MutableList<DailyMenu> = mutableListOf(),

    @OneToMany(mappedBy = "menuItem")
    val recipes: MutableList<Recipe> = mutableListOf()
)