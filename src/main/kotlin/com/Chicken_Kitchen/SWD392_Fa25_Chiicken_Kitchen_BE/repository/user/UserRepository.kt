package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.user

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.user.User
import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.enum.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
    fun findAllByRole(role: Role): List<User>
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean
}
