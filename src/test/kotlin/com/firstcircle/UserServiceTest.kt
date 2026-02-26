package com.firstcircle

import com.firstcircle.balance.UserBalanceRepository
import com.firstcircle.user.UserRepository
import com.firstcircle.user.UserService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.math.BigDecimal

class UserServiceTest : FunSpec({

    lateinit var userService: UserService
    lateinit var userBalanceRepository: UserBalanceRepository

    beforeEach {
        userBalanceRepository = UserBalanceRepository()
        userService = UserService(UserRepository(), userBalanceRepository)
    }

    context("createUser") {
        test("should create user and return it with a generated id") {
            val user = userService.createUser("John", "Doe")

            user.id shouldNotBe null
            user.firstName shouldBe "John"
            user.lastName shouldBe "Doe"
        }

        test("should create user with default zero balance when no initial balance given") {
            val user = userService.createUser("John", "Doe")
            val balance = userBalanceRepository.findUserBalanceById(user.id.toString())

            balance.balance shouldBe BigDecimal.ZERO
        }

        test("should create user with given initial balance") {
            val user = userService.createUser("Jane", "Smith", BigDecimal("500.00"))
            val balance = userBalanceRepository.findUserBalanceById(user.id.toString())

            balance.balance shouldBe BigDecimal("500.00")
        }

        test("should create multiple users with independent balances") {
            val alice = userService.createUser("Alice", "A", BigDecimal("100.00"))
            val bob = userService.createUser("Bob", "B", BigDecimal("200.00"))

            userBalanceRepository.findUserBalanceById(alice.id.toString()).balance shouldBe BigDecimal("100.00")
            userBalanceRepository.findUserBalanceById(bob.id.toString()).balance shouldBe BigDecimal("200.00")
        }
    }

    context("findUserById") {
        test("should return the user when found") {
            val created = userService.createUser("Alice", "Wong")
            val found = userService.findUserById(created.id.toString())

            found shouldBe created
        }

        test("should throw when user does not exist") {
            shouldThrow<IllegalStateException> {
                userService.findUserById("non-existent-id")
            }
        }
    }
})
