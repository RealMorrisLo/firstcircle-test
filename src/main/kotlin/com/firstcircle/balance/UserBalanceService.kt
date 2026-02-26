package com.firstcircle.balance

import java.math.BigDecimal

class UserBalanceService(
    private val userBalanceRepository: UserBalanceRepository
) {
    fun getUserBalanceById(id: String): UserBalance {
        return userBalanceRepository.findUserBalanceById(id)
    }

    fun transfer(fromUserId: String, toUserId: String, amount: BigDecimal) {
        require(amount > BigDecimal.ZERO) { "amount must be positive" }
        require(fromUserId != toUserId) { "Transfer not allowed for same user id" }
        userBalanceRepository.findUserBalanceById(userId = fromUserId)
        userBalanceRepository.findUserBalanceById(toUserId)

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
