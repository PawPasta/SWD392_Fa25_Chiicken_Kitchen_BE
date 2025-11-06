package com.ChickenKitchen.app.repository.menu

import com.ChickenKitchen.app.model.entity.menu.MenuItemNutrient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface MenuItemNutrientRepository : JpaRepository<MenuItemNutrient, Long>{
    fun findByMenuItemId(menuItemId: Long): List<MenuItemNutrient>
}