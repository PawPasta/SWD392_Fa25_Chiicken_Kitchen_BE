package com.ChickenKitchen.app.repository.menu

import com.ChickenKitchen.app.model.entity.menu.MenuItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository


@Repository
interface MenuItemRepository : JpaRepository<MenuItem, Long> , JpaSpecificationExecutor<MenuItem>{
}