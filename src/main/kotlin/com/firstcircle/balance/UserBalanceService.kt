package com.firstcircle.balance

import java.math.BigDecimal

class UserBalanceService(
    private val userBalanceRepository: UserBalanceRepository
) {
    fun transfer(fromUserId: String, toUserId: String, amount: BigDecimal) {
        require(amount > BigDecimal.ZERO) { "amount must be positive" }
        val fromUserBalance = userBalanceRepository.findUserBalanceById(userId = fromUserId)
        userBalanceRepository.findUserBalanceById(toUserId)

        val newFromUserBalance = fromUserBalance.balance
        require(newFromUserBalance.minus(amount) < BigDecimal.ZERO) {
            "Balance can't be negative"
        }

        withdraw(fromUserId, amount)
        deposit(toUserId, amount)
    }

    fun deposit(userId: String, amount: BigDecimal): UserBalance {
        if (amount <= BigDecimal.ZERO) { error("Deposit amount must be greater than zero.") }
        return userBalanceRepository.updateUserBalance(userId = userId, amount = amount)
    }

    fun withdraw(userId: String, amount: BigDecimal): UserBalance {
        if (amount <= BigDecimal.ZERO) { error("Withdraw amount must be greater than zero.") }
        return userBalanceRepository.updateUserBalance(userId = userId, amount = amount.negate())
    }
}
