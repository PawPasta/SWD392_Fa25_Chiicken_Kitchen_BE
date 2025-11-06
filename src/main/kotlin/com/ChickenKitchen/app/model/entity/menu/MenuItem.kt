package com.ChickenKitchen.app.model.entity.menu

import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.model.entity.ingredient.Recipe
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "image_url", length = 500)
    var imageUrl: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @Column(name = "price", nullable = false)
    var price : Int =0,

    @Column(name = "cal", nullable = false)
    var cal: Int = 0,

    @Column(name = "description")
    var description: String? = null,

    @OneToMany(mappedBy = "menuItem")
    val recipes: MutableList<Recipe> = mutableListOf()
)
