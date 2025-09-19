package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.repository.auth

import com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.model.entity.user.MailToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MailTokenRepository : JpaRepository<MailToken, Long> {
    fun findByToken(token: String): Optional<MailToken>
    fun findAllByUserId(userId: Long): List<MailToken>
    fun deleteByToken(token: String)
}
