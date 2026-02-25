package com.firstcircle

import com.firstcircle.balance.UserBalanceRepository
import com.firstcircle.balance.UserBalanceService
import com.firstcircle.user.UserRepository
import com.firstcircle.user.UserService
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080) {
        // --- Dependency Injection ---
        val userRepository = UserRepository()
        val userBalanceRepository = UserBalanceRepository()
        val userService = UserService(userRepository, userBalanceRepository)
        val userBalanceService = UserBalanceService(userBalanceRepository)

        // --- Plugins ---
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(CORS) {
            allowHost("*")
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowHeader(HttpHeaders.ContentType)
        }

        install(CallLogging)

        // --- Routes ---
        routing {
            get("/") {
                call.respond(mapOf("message" to "Hello from Ktor!", "status" to "ok"))
            }

            get("/health") {
                call.respond(mapOf("status" to "healthy"))
            }

            // Pass services into route modules
            userRoutes(userService)
            userBalanceRoutes(userBalanceService)
        }
    }.start(wait = true)
}
