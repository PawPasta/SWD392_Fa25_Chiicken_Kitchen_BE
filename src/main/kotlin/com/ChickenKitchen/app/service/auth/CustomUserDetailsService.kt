package com.ChickenKitchen.app.service.auth

import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.authority.SimpleGrantedAuthority
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.handler.UserNotFoundException
import org.springframework.security.core.userdetails.User

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found: $username")

        return User(
            user.username,
            user.password,
            listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
        )
    }
}
