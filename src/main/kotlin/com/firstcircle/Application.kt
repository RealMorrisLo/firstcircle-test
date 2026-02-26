package com.firstcircle

import com.firstcircle.balance.UserBalanceRepository
import com.firstcircle.balance.UserBalanceService
import com.firstcircle.user.UserRepository
import com.firstcircle.user.UserService
import java.math.BigDecimal

fun main() {
    val userRepository = UserRepository()
    val userBalanceRepository = UserBalanceRepository()
    val userService = UserService(userRepository, userBalanceRepository)
    val userBalanceService = UserBalanceService(userBalanceRepository)

    println("=== First Circle Banking Service ===")
    println("Type 'help' for available commands, 'exit' to quit.")
    println()

    runRepl(userService, userBalanceService)
}

private fun runRepl(userService: UserService, userBalanceService: UserBalanceService) {
    while (true) {
        print("> ")
        val input = readlnOrNull()?.trim() ?: return
        if (input.isNotBlank() && !dispatch(input, userService, userBalanceService)) return
    }
}

private fun dispatch(input: String, userService: UserService, userBalanceService: UserBalanceService): Boolean {
    val params = input.split(" ")
    if (params[0] == "exit" || params[0] == "quit") {
        println("Goodbye!")
        return false
    }

    when (params[0]) {
        "help" -> printHelp()
        "create-user" -> handleCreateUser(params, userService)
        "get-user" -> handleGetUser(params, userService)
        "balance" -> handleBalance(params, userBalanceService)
        "deposit" -> handleDeposit(params, userBalanceService)
        "withdraw" -> handleWithdraw(params, userBalanceService)
        "transfer" -> handleTransfer(params, userBalanceService)
        else -> println("Unknown command '${params[0]}'. Type 'help' for available commands.")
    }

    return true
}

private fun handleCreateUser(params: List<String>, userService: UserService) {
    if (params.size < 3) {
        println("Usage: create-user <firstName> <lastName> [initialBalance]")
        return
    }
    val initialBalance = params.getOrNull(3)?.toBigDecimalOrNull() ?: BigDecimal.ZERO
    runCatching { userService.createUser(params[1], params[2], initialBalance) }
        .onSuccess { println("Created: id=${it.id}  name=${it.firstName} ${it.lastName}  balance=$initialBalance") }
        .onFailure { println("Error: ${it.message}") }
}

private fun handleGetUser(params: List<String>, userService: UserService) {
    if (params.size < 2) {
        println("Usage: get-user <userId>")
        return
    }
    runCatching { userService.findUserById(params[1]) }
        .onSuccess { println("User: id=${it.id}  name=${it.firstName} ${it.lastName}") }
        .onFailure { println("Error: ${it.message}") }
}

private fun handleBalance(params: List<String>, userBalanceService: UserBalanceService) {
    if (params.size < 2) {
        println("Usage: balance <userId>")
        return
    }
    runCatching { userBalanceService.getUserBalanceById(params[1]) }
        .onSuccess { println("Balance [${it.userId}]: ${it.balance}") }
        .onFailure { println("Error: ${it.message}") }
}

private fun handleDeposit(params: List<String>, userBalanceService: UserBalanceService) {
    if (params.size < 3) {
        println("Usage: deposit <userId> <amount>")
        return
    }
    val amount = params[2].toBigDecimalOrNull() ?: run {
        println("Invalid amount.")
        return
    }
    runCatching { userBalanceService.deposit(params[1], amount) }
        .onSuccess { println("Deposited $amount -> new balance: ${it.balance}") }
        .onFailure { println("Error: ${it.message}") }
}

private fun handleWithdraw(params: List<String>, userBalanceService: UserBalanceService) {
    if (params.size < 3) {
        println("Usage: withdraw <userId> <amount>")
        return
    }
    val amount = params[2].toBigDecimalOrNull() ?: run {
        println("Invalid amount.")
        return
    }
    runCatching { userBalanceService.withdraw(params[1], amount) }
        .onSuccess { println("Withdrew $amount -> new balance: ${it.balance}") }
        .onFailure { println("Error: ${it.message}") }
}

private fun handleTransfer(params: List<String>, userBalanceService: UserBalanceService) {
    if (params.size < 4) {
        println("Usage: transfer <fromUserId> <toUserId> <amount>")
        return
    }
    val amount = params[3].toBigDecimalOrNull() ?: run {
        println("Invalid amount.")
        return
    }
    runCatching { userBalanceService.transfer(params[1], params[2], amount) }
        .onSuccess { println("Transferred $amount from ${params[1]} to ${params[2]}") }
        .onFailure { println("Error: ${it.message}") }
}

private fun printHelp() {
    println(
        """
        Commands:
          create-user <firstName> <lastName> [initialBalance]  - Open a new account
          get-user <userId>                                     - Look up a user
          balance <userId>                                      - Check account balance
          deposit <userId> <amount>                             - Deposit money
          withdraw <userId> <amount>                            - Withdraw money
          transfer <fromUserId> <toUserId> <amount>             - Transfer between accounts
          help                                                  - Show this message
          exit                                                  - Quit
        """.trimIndent()
    )
}
