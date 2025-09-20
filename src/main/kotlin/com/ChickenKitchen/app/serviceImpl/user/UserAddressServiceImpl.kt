package com.ChickenKitchen.app.serviceImpl.user

import org.springframework.stereotype.Service
import com.ChickenKitchen.app.model.entity.user.UserAddress
import com.ChickenKitchen.app.model.dto.response.UserAddressResponse
import com.ChickenKitchen.app.model.dto.request.CreateUserAddressRequest
import com.ChickenKitchen.app.model.dto.request.UpdateUserAddressRequest
import com.ChickenKitchen.app.service.user.UserAddressService
import com.ChickenKitchen.app.repository.user.UserAddressRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import com.ChickenKitchen.app.mapper.toUserAddressResponse
import com.ChickenKitchen.app.mapper.toUserAddressResponseList

@Service
class UserAddressServiceImpl (
    private val userRepository: UserRepository,
    private val userAddressRepository: UserAddressRepository
) : UserAddressService {

    override fun getAll(): List<UserAddressResponse> {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("User not found")
        val addresses = userAddressRepository.findAllByUserId(userId = user.id!!)

        if (addresses.isEmpty()) throw IllegalArgumentException("No addresses found for user")

        return addresses.toUserAddressResponseList()
    }

    override fun getById(id: Long): UserAddressResponse {
        val username = SecurityContextHolder.getContext().authentication.name

        val address = userAddressRepository.findAllByUserUsernameAndId(userUsername = username, id = id).firstOrNull()
            ?: throw IllegalArgumentException("Address not found")

        return address.toUserAddressResponse()
    }

    override fun create(req: CreateUserAddressRequest): UserAddressResponse {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username)
            ?: throw IllegalArgumentException("User not found")

        val userAddress = UserAddress(
            user = user,
            recipientName = req.recipientName,
            phone = req.phone,
            addressLine = req.addressLine,
            city = req.city,
            isDefault = req.isDefault
        )

        if (req.isDefault) {
            val currentDefaultAddress = userAddressRepository.findAllByUserUsernameAndIsDefaultTrue(username)
            if (currentDefaultAddress.isNotEmpty()) {
                currentDefaultAddress[0].isDefault = false
                userAddressRepository.save(currentDefaultAddress[0])
            }
        }

        val savedAddress = userAddressRepository.save(userAddress)

        return savedAddress.toUserAddressResponse()
    }

    override fun update(id: Long, req: UpdateUserAddressRequest): UserAddressResponse {
        val username = SecurityContextHolder.getContext().authentication.name

        val address = userAddressRepository.findAllByUserUsernameAndId(userUsername = username, id = id).firstOrNull()
            ?: throw IllegalArgumentException("Address not found")

        address.recipientName = req.recipientName ?: address.recipientName
        address.phone = req.phone ?: address.phone
        address.addressLine = req.addressLine ?: address.addressLine
        address.city = req.city ?: address.city
        address.isDefault = req.isDefault ?: address.isDefault
        val savedAddress = userAddressRepository.save(address)

        return savedAddress.toUserAddressResponse()
    }


    override fun delete(id: Long) {
        val username = SecurityContextHolder.getContext().authentication.name

        val address = userAddressRepository.findAllByUserUsernameAndId(userUsername = username, id = id).firstOrNull()
            ?: throw IllegalArgumentException("Address not found")

        userAddressRepository.delete(address)
    }

    override fun setDefault(id: Long) {
        val username = SecurityContextHolder.getContext().authentication.name

        val address = userAddressRepository.findAllByUserUsernameAndId(userUsername = username, id = id).firstOrNull()
            ?: throw IllegalArgumentException("Address not found")

        val currentDefaultAddress = userAddressRepository.findAllByUserUsernameAndIsDefaultTrue(username)

        if (currentDefaultAddress.isNotEmpty() && currentDefaultAddress[0].id != address.id) {
            currentDefaultAddress[0].isDefault = false
            userAddressRepository.save(currentDefaultAddress[0])
        }

        address.isDefault = true
        userAddressRepository.save(address)
    }
}
