package com.ChickenKitchen.app.repository.user

import com.ChickenKitchen.app.model.entity.user.EmployeeDetail
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.model.entity.ingredient.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeDetailRepository : JpaRepository<EmployeeDetail, Long> {
    fun findByUser(user: User): EmployeeDetail?
    fun findAllByStore(store: Store): List<EmployeeDetail>
}

