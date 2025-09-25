package com.ChickenKitchen.app.repository.category

import com.ChickenKitchen.app.model.entity.category.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByName(name: String): Optional<Category>
}