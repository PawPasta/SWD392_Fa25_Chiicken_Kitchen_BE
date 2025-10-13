package com.ChickenKitchen.app.serviceImpl.user

import com.ChickenKitchen.app.service.user.UserService
import org.springframework.stereotype.Service
import org.springframework.security.core.context.SecurityContextHolder
import com.ChickenKitchen.app.model.dto.response.UserResponse
import com.ChickenKitchen.app.model.dto.response.UserDetailResponse
import com.ChickenKitchen.app.model.dto.request.CreateUserRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserProfileRequest
import com.ChickenKitchen.app.model.dto.response.UserProfileResponse
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.repository.auth.UserSessionRepository
import com.ChickenKitchen.app.mapper.toUserResponseList
import com.ChickenKitchen.app.mapper.toUserResponse
import com.ChickenKitchen.app.mapper.toUserDetailResponse
import com.ChickenKitchen.app.mapper.toUserProfileResponse
import com.ChickenKitchen.app.handler.UserNotFoundException
import java.sql.Timestamp

@Service
class UserServiceImpl (
    private val userRepository: UserRepository,
    private val userSessionRepository: UserSessionRepository
): UserService {

    override fun getAll() : List<UserResponse>? {
        val users = userRepository.findAll()
        if (users.isEmpty()) return null
        return users.toUserResponseList()
    }

    override fun getById(id: Long) : UserDetailResponse {
        val user = userRepository.findById(id).orElse(null) ?: throw UserNotFoundException("User with id $id not found")
        return user.toUserDetailResponse()
    }

    override fun create(req: CreateUserRequest) : UserDetailResponse {
        if (req.email.isNullOrEmpty()) {
            throw IllegalArgumentException("Email is required")
        }
        val newUser = userRepository.save(
            User(
                fullName = req.fullName,
                email = req.email,
                birthday = req.birthday,
                role = req.role,
                isActive = req.isActive,
                imageURL = req.imageURL,
                password = "123",
                provider = "local"
            )
        )
        return newUser.toUserDetailResponse()
    }

    override fun update(id: Long, req: UpdateUserRequest) : UserDetailResponse {
        val user = userRepository.findById(id).orElse(null) ?: throw UserNotFoundException("User with id $id not found")
        if (!req.fullName.isNullOrEmpty()) {
            user.fullName = req.fullName
        }
        if (req.birthday != null) {
            user.birthday = req.birthday
        }
        if (req.role != null) {
            user.role = req.role
        }
        if (req.isActive != null) {
            user.isActive = req.isActive
        }
        val updatedUser = userRepository.save(user)
        return updatedUser.toUserDetailResponse()
    }

    override fun delete(id: Long) {
        val user = userRepository.findById(id).orElse(null) ?: throw UserNotFoundException("User with id $id not found")
        userRepository.delete(user)
    }

    override fun changeStatus(id: Long) : UserResponse {
        val user = userRepository.findById(id).orElse(null) ?: throw UserNotFoundException("User with id $id not found")
        user.isActive = !user.isActive
        val updatedUser = userRepository.save(user)
        return updatedUser.toUserResponse()
    }

    override fun getProfile() : UserProfileResponse {
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email) ?: throw UserNotFoundException("User not found")
        return user.toUserProfileResponse()
    }

    override fun updateProfile(req: UpdateUserProfileRequest) : UserProfileResponse {
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email) ?: throw UserNotFoundException("User not found")

        if (!req.fullName.isNullOrEmpty()) {
            user.fullName = req.fullName
        }
        if (req.birthday != null) {
            user.birthday = req.birthday
        }

        val updatedUser = userRepository.save(user)
        val userSessions = userSessionRepository.findAllByUserEmailAndIsCancelledFalse(email)
        userSessions[0].lastActivity = Timestamp(System.currentTimeMillis())
        userSessionRepository.saveAll(userSessions)

        return updatedUser.toUserProfileResponse()
    }

}
