package com.firstcircle.balance

import com.firstcircle.user.User
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

class UserBalanceRepository {
    val userBalanceLookup = ConcurrentHashMap<String, UserBalance>()

    fun createUserBalance(user: User, initialBalance: BigDecimal): UserBalance {
        val userId = user.id.toString()
        val balance = UserBalance(userId, initialBalance)
        userBalanceLookup[userId] = balance

        return balance
    }

    fun findUserBalanceById(userId: String): UserBalance =
        userBalanceLookup[userId] ?: error("User balance with id $userId not found")

    fun updateUserBalance(userId: String, amount: BigDecimal): UserBalance {
        if (amount == BigDecimal.ZERO) {
            error("Zero amount not allowed.")
        }
        val userBalance = findUserBalanceById(userId)

        val newBalance = userBalance.balance + amount
        if (newBalance <= BigDecimal.ZERO) {
            error("Insufficient balance for user $userId.")
        }

        userBalanceLookup[userId] = userBalance.copy(balance = newBalance)

        return userBalance
    }
}
