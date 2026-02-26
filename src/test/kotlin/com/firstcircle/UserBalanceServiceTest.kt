package com.firstcircle

import com.firstcircle.balance.UserBalanceRepository
import com.firstcircle.balance.UserBalanceService
import com.firstcircle.user.UserRepository
import com.firstcircle.user.UserService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal

class UserBalanceServiceTest : FunSpec({

    lateinit var userService: UserService
    lateinit var userBalanceService: UserBalanceService

    beforeEach {
        val userBalanceRepository = UserBalanceRepository()
        userService = UserService(UserRepository(), userBalanceRepository)
        userBalanceService = UserBalanceService(userBalanceRepository)
    }

    context("getUserBalanceById") {
        test("should return balance for existing user") {
            val user = userService.createUser("Alice", "Wong", BigDecimal("500.00"))

            val balance = userBalanceService.getUserBalanceById(user.id.toString())

            balance.balance shouldBe BigDecimal("500.00")
        }

        test("should throw when user balance does not exist") {
            shouldThrow<IllegalStateException> {
                userBalanceService.getUserBalanceById("non-existent-id")
            }
        }
    }

    context("deposit") {
        test("should add amount to existing balance") {
            val user = userService.createUser("Bob", "Lee", BigDecimal("100.00"))

            val result = userBalanceService.deposit(user.id.toString(), BigDecimal("50.00"))

            result.balance shouldBe BigDecimal("150.00")
        }

        test("should throw when depositing zero") {
            val user = userService.createUser("Bob", "Lee", BigDecimal("100.00"))

            shouldThrow<IllegalStateException> {
                userBalanceService.deposit(user.id.toString(), BigDecimal.ZERO)
            }
        }

        test("should throw when depositing a negative amount") {
            val user = userService.createUser("Bob", "Lee", BigDecimal("100.00"))

            shouldThrow<IllegalStateException> {
                userBalanceService.deposit(user.id.toString(), BigDecimal("-10.00"))
            }
        }
    }

    context("withdraw") {
        test("should subtract amount from existing balance") {
            val user = userService.createUser("Carol", "Tan", BigDecimal("200.00"))

            val result = userBalanceService.withdraw(user.id.toString(), BigDecimal("80.00"))

            result.balance shouldBe BigDecimal("120.00")
        }

        test("should throw when withdrawing more than available balance") {
            val user = userService.createUser("Carol", "Tan", BigDecimal("10.00"))

            shouldThrow<IllegalStateException> {
                userBalanceService.withdraw(user.id.toString(), BigDecimal("999.00"))
            }
        }

        test("should throw when withdrawing zero") {
            val user = userService.createUser("Carol", "Tan", BigDecimal("100.00"))

            shouldThrow<IllegalStateException> {
                userBalanceService.withdraw(user.id.toString(), BigDecimal.ZERO)
            }
        }

        test("should throw when withdrawing a negative amount") {
            val user = userService.createUser("Carol", "Tan", BigDecimal("100.00"))

            shouldThrow<IllegalStateException> {
                userBalanceService.withdraw(user.id.toString(), BigDecimal("-10.00"))
            }
        }

        test("should allow withdrawing entire balance") {
            val user = userService.createUser("Carol", "Tan", BigDecimal("100.00"))

            val result = userBalanceService.withdraw(user.id.toString(), BigDecimal("100.00"))

            result.balance.compareTo(BigDecimal.ZERO) shouldBe 0
        }
    }

    context("transfer") {
        test("should move amount from sender to receiver") {
            val from = userService.createUser("Eve", "Park", BigDecimal("300.00"))
            val to = userService.createUser("Frank", "Kim", BigDecimal("100.00"))

            userBalanceService.transfer(from.id.toString(), to.id.toString(), BigDecimal("100.00"))

            userBalanceService.getUserBalanceById(from.id.toString()).balance shouldBe BigDecimal("200.00")
            userBalanceService.getUserBalanceById(to.id.toString()).balance shouldBe BigDecimal("200.00")
        }

        test("should throw when sender has insufficient balance") {
            val from = userService.createUser("Eve", "Park", BigDecimal("50.00"))
            val to = userService.createUser("Frank", "Kim", BigDecimal("100.00"))

            shouldThrow<IllegalArgumentException> {
                userBalanceService.transfer(from.id.toString(), to.id.toString(), BigDecimal("100.00"))
            }
        }

        test("should throw when transferring zero amount") {
            val from = userService.createUser("Eve", "Park", BigDecimal("300.00"))
            val to = userService.createUser("Frank", "Kim", BigDecimal("100.00"))

            shouldThrow<IllegalArgumentException> {
                userBalanceService.transfer(from.id.toString(), to.id.toString(), BigDecimal.ZERO)
            }
        }

        test("should throw when receiver does not exist") {
            val from = userService.createUser("Eve", "Park", BigDecimal("300.00"))

            shouldThrow<IllegalStateException> {
                userBalanceService.transfer(from.id.toString(), "non-existent-id", BigDecimal("50.00"))
            }
        }

        test("should not deduct from sender when receiver does not exist") {
            val from = userService.createUser("Eve", "Park", BigDecimal("300.00"))

            runCatching { userBalanceService.transfer(from.id.toString(), "non-existent-id", BigDecimal("50.00")) }

            userBalanceService.getUserBalanceById(from.id.toString()).balance shouldBe BigDecimal("300.00")
        }
    }
})
