package com.ChickenKitchen.app.serviceImpl.user

import com.ChickenKitchen.app.handler.UserCannotDeleteException
import com.ChickenKitchen.app.handler.UserEmailRequiredException
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
import com.ChickenKitchen.app.repository.user.WalletRepository
import com.ChickenKitchen.app.mapper.toUserResponseList
import com.ChickenKitchen.app.mapper.toUserResponse
import com.ChickenKitchen.app.mapper.toUserDetailResponse
import com.ChickenKitchen.app.mapper.toUserProfileResponse
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.model.dto.request.GrantRoleRequest
import com.ChickenKitchen.app.enums.Role
import com.ChickenKitchen.app.model.entity.user.Wallet
import com.ChickenKitchen.app.util.EmailUtil
import java.sql.Timestamp

@Service
class UserServiceImpl (
    private val userRepository: UserRepository,
    private val userSessionRepository: UserSessionRepository,
    private val walletRepository: WalletRepository,
    private val emailUtil: EmailUtil,
): UserService {

    override fun getAll() : List<UserResponse>? {
        val users = userRepository.findAll()
        if (users.isEmpty()) return null
        return users.toUserResponseList()
    }

    override fun getById(id: Long) : UserDetailResponse {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User with id $id not found") }
        return user.toUserDetailResponse()
    }

    override fun create(req: CreateUserRequest) : UserDetailResponse {
        if (req.email.isEmpty()) {
            throw UserEmailRequiredException("Email is required to create a user")
        }
        val newUser = userRepository.save(
            User(
                fullName = req.fullName,
                email = req.email,
                birthday = req.birthday,
                role = req.role,
                isActive = req.isActive,
                imageURL = req.imageURL,
                provider = "local"
            )
        )
        return newUser.toUserDetailResponse()
    }

    override fun update(id: Long, req: UpdateUserRequest) : UserDetailResponse {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User with id $id not found") }

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
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User with id $id not found") }

        if (user.orders.isNotEmpty()) {
            throw UserCannotDeleteException("Cannot delete user with id $id: has ${user.orders.size} associated orders")
        }
        userRepository.delete(user)
    }

    override fun changeStatus(id: Long) : UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User with id $id not found") }
        user.isActive = !user.isActive
        val updatedUser = userRepository.save(user)
        return updatedUser.toUserResponse()
    }

    override fun getProfile() : UserProfileResponse {
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException("User with email $email not found")
        return user.toUserProfileResponse()
    }

    override fun updateProfile(req: UpdateUserProfileRequest) : UserProfileResponse {
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException("User with email $email not found")

        req.fullName?.let { user.fullName = it }
        req.birthday?.let { user.birthday = it }

        val updatedUser = userRepository.save(user)


        val userSessions = userSessionRepository.findAllByUserEmailAndIsCancelledFalse(email)
        val now = Timestamp(System.currentTimeMillis())
        userSessions.forEach { it.lastActivity = now }
        userSessionRepository.saveAll(userSessions)

        return updatedUser.toUserProfileResponse()
    }

    override fun grantRoleByEmail(req: GrantRoleRequest): UserDetailResponse {
        if (req.email.isBlank()) throw UserEmailRequiredException("Email is required")
        if (req.role == Role.ADMIN) throw IllegalArgumentException("Cannot grant ADMIN role")

        val existing = userRepository.findByEmail(req.email)
        val isNew = existing == null

        val user = existing ?: userRepository.save(
            User(
                fullName = req.fullName ?: req.email.substringBefore('@'),
                email = req.email,
                role = req.role,
                isActive = true,
                isVerified = true,
                imageURL = null,
                provider = "admin-grant"
            )
        )

        if (!isNew) {
            user.role = req.role
        }
        val saved = userRepository.save(user)

        var wallet = walletRepository.findByUser(saved)
        if (wallet == null) {
            wallet = Wallet(user = saved, balance = 0)
            walletRepository.save(wallet)
        }

        try {
            if (isNew) {
                emailUtil.send(
                    to = saved.email,
                    subject = "Chicken Kitchen: Tài khoản được cấp quyền",
                    content = "Xin chào ${saved.fullName},\n\nTài khoản của bạn đã được cấp quyền ${req.role}. Hãy đăng nhập để sử dụng.\n\nCảm ơn!",
                    isHtml = false
                )
            } else {
                emailUtil.send(
                    to = saved.email,
                    subject = "Chicken Kitchen: Quyền tài khoản được cập nhật",
                    content = "Xin chào ${saved.fullName},\n\nQuyền truy cập của bạn đã được cập nhật thành ${req.role}.\n\nCảm ơn!",
                    isHtml = false
                )
            }
        } catch (_: Exception) {
            // Do not fail the operation if email dispatch fails
        }

        return saved.toUserDetailResponse()
    }

    override fun getAllRoles(): List<Role> {
        return Role.values().toList()
    }

}
