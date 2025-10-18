package com.ChickenKitchen.app.model.entity.menu

import com.ChickenKitchen.app.enums.UnitType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "nutrients")
class Nutrient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "base_unit", nullable = false)
    val baseUnit: UnitType,

    @OneToMany(mappedBy = "nutrient")
    val menuItemNutrients: MutableList<MenuItemNutrient> = mutableListOf()
)