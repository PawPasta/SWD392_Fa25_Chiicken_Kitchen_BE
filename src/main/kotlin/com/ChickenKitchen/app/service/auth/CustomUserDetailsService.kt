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

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException("User not found: $email")

        // Principal is the email; password is not used for Google/JWT validation
        return User(
            user.email,
            "",
            listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
        )
    }
}
