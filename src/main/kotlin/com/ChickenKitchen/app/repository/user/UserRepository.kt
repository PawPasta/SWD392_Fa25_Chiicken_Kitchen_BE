package com.ChickenKitchen.app.repository.user

import com.ChickenKitchen.app.enum.Role
import com.ChickenKitchen.app.model.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findAllByRole(role: Role): List<User>
    fun existsByEmail(email: String): Boolean
}
