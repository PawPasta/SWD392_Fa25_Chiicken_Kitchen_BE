package com.ChickenKitchen.app.repository.step

import com.ChickenKitchen.app.model.entity.step.Dish
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface DishRepository : JpaRepository<Dish, Long>{
}