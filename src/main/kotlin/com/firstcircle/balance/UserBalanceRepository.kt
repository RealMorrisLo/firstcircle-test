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
        if (amount == BigDecimal.ZERO) error("Zero amount not allowed.")

        val result = userBalanceLookup.compute(userId) { _, existing ->
            val balance = existing ?: error("User balance with id $userId not found")
            val newBalance = balance.balance + amount
            if (newBalance <= BigDecimal.ZERO) error("Insufficient balance for user $userId.")
            balance.copy(balance = newBalance)
        }

        return result ?: error("User balance with id $userId not found")
    }
}
