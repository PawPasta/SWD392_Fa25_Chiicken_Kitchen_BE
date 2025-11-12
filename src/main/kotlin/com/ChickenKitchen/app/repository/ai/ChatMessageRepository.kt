package com.ChickenKitchen.app.repository.ai

import com.ChickenKitchen.app.model.entity.ai.ChatMessage
import com.ChickenKitchen.app.model.entity.auth.UserSession
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    fun findAllByUserSessionOrderByCreatedAtAsc(userSession: UserSession): List<ChatMessage>
    fun findAllByUserSession(userSession: UserSession, pageable: Pageable): Page<ChatMessage>
}
