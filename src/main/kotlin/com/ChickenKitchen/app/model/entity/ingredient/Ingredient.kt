package com.ChickenKitchen.app.model.entity.ingredient

import com.ChickenKitchen.app.enum.UnitType
import jakarta.persistence.*
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp


@Entity
@Table(name = "ingredients")
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    val name: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "base_unit")
    val baseUnit: UnitType? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @Column(name = "image_url")
    val imageUrl: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "batch_number", nullable = false)
    val batchNumber: String,

    @OneToMany(mappedBy = "ingredient")
    val storeBatches: MutableList<StoreIngredientBatch> = mutableListOf(),

    @OneToMany(mappedBy = "ingredient")
    val recipes: MutableList<Recipe> = mutableListOf()
)