package com.firstcircle

import com.firstcircle.balance.UserBalanceRepository
import com.firstcircle.balance.UserBalanceService
import com.firstcircle.route.userBalanceRoutes
import com.firstcircle.route.userRoutes
import com.firstcircle.user.UserRepository
import com.firstcircle.user.UserService
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    val userRepository = UserRepository()
    val userBalanceRepository = UserBalanceRepository()
    val userService = UserService(userRepository, userBalanceRepository)
    val userBalanceService = UserBalanceService(userBalanceRepository)

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
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

    routing {
        get("/") {
            call.respond(mapOf("message" to "Hello from Ktor!", "status" to "ok"))
        }

        get("/health") {
            call.respond(mapOf("status" to "healthy"))
        }

        userRoutes(userService)
        userBalanceRoutes(userBalanceService)
    }
}
