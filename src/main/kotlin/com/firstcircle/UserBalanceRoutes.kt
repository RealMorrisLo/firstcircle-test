package com.firstcircle

import com.firstcircle.balance.UserBalanceService
import io.ktor.http.*
import io.ktor.server.application.call
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.math.BigDecimal

fun Routing.userBalanceRoutes(userBalanceService: UserBalanceService) {
    route("/balances") {
        post("/deposit") {
            val params = call.receive<Map<String, String>>()
            val userId = params["userId"] ?: return@post call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "userId is required")
            )
            val amount = params["amount"]?.let { BigDecimal(it) } ?: return@post call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "amount is required")
            )
            runCatching { userBalanceService.deposit(userId, amount) }
                .onSuccess { call.respond(it) }
                .onFailure { call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message)) }
        }

        post("/withdraw") {
            val params = call.receive<Map<String, String>>()
            val userId = params["userId"] ?: return@post call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "userId is required")
            )
            val amount = params["amount"]?.let { BigDecimal(it) } ?: return@post call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "amount is required")
            )
            runCatching { userBalanceService.withdraw(userId, amount) }
                .onSuccess { call.respond(it) }
                .onFailure { call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message)) }
        }

        post("/transfer") {
            val params = call.receive<Map<String, String>>()
            val fromUserId = params["fromUserId"] ?: return@post call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "fromUserId is required")
            )
            val toUserId = params["toUserId"] ?: return@post call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "toUserId is required")
            )
            val amount = params["amount"]?.let { BigDecimal(it) } ?: return@post call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "amount is required")
            )
            runCatching { userBalanceService.transfer(fromUserId, toUserId, amount) }
                .onSuccess { call.respond(mapOf("status" to "transfer successful")) }
                .onFailure { call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message)) }
        }
    }
}
