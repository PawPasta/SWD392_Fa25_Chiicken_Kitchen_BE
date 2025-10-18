package com.ChickenKitchen.app.model.entity.ingredient

import com.ChickenKitchen.app.enums.UnitType
import jakarta.persistence.*
import java.sql.Timestamp
import org.hibernate.annotations.CreationTimestamp


@Entity
@Table(name = "ingredients")
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    var name: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "base_unit")
    var baseUnit: UnitType? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Timestamp? = null,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "batch_number", nullable = false, unique = true)
    var batchNumber: String,

    @OneToMany(mappedBy = "ingredient")
    val storeBatches: MutableList<StoreIngredientBatch> = mutableListOf(),

    @OneToMany(mappedBy = "ingredient")
    val recipes: MutableList<Recipe> = mutableListOf()
)