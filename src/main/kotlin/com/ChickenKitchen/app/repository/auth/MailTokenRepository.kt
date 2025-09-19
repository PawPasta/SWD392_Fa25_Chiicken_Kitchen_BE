package com.ChickenKitchen.app.repository.auth

import com.ChickenKitchen.app.model.entity.auth.MailToken
import com.ChickenKitchen.app.enum.MailType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MailTokenRepository : JpaRepository<MailToken, Long> {
    fun findByToken(token: String): MailToken?
    fun findAllByUserId(userId: Long): List<MailToken>
    fun findAllByTokenAndTypeAndIsCanceledFalse(token: String, type: MailType): List<MailToken>
    fun findAllByUserIdAndTypeAndIsCanceledFalse(userId: Long, type: MailType): List<MailToken>
    fun deleteByToken(token: String)
}
