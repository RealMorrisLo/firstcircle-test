package com.firstcircle.user

import com.firstcircle.balance.UserBalanceRepository
import java.math.BigDecimal

class UserService(
    private val userRepository: UserRepository,
    private val userBalanceRepository: UserBalanceRepository
) {
    fun createUser(firstName: String, lastName: String, initialBalance: BigDecimal = BigDecimal.ZERO): User {
        val user = userRepository.createUser(firstName, lastName)
        userBalanceRepository.createUserBalance(user, initialBalance)
        return user
    }

    fun findUserById(id: String): User = userRepository.findUserById(id) ?: error("User with id $id not found")
}
