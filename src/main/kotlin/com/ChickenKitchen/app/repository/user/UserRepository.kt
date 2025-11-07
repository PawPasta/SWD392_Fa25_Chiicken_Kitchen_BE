package com.ChickenKitchen.app.repository.user

import com.ChickenKitchen.app.enums.Role
import com.ChickenKitchen.app.model.dto.response.UserGrowthProjection
import com.ChickenKitchen.app.model.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Timestamp


@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findAllByRole(role: Role): List<User>
    fun existsByEmail(email: String): Boolean
    fun countByRole(role: Role): Long

    @Query(
        """
        SELECT new com.ChickenKitchen.app.model.dto.response.UserGrowthProjection(
            CAST(u.createdAt AS date),
            COUNT(u)
        )
        FROM User u
        WHERE u.createdAt >= :fromDate
        GROUP BY CAST(u.createdAt AS date)
        ORDER BY CAST(u.createdAt AS date)
        """
    )
    fun countUsersGroupedByDateSince(@Param("fromDate") fromDate: Timestamp): List<UserGrowthProjection>
}
