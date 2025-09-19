package com.Chicken_Kitchen.repository.combo

import com.Chicken_Kitchen.model.entity.combo.ComboItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ComboItemRepository : JpaRepository<ComboItem, Long> {
    fun findAllByComboId(comboId: Long): List<ComboItem>
    fun findAllByRecipeId(recipeId: Long): List<ComboItem>
}
