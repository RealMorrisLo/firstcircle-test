package com.firstcircle

import com.firstcircle.user.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.math.BigDecimal

fun Routing.userRoutes(userService: UserService) {
    route("/users") {
        post {
            val params = call.receive<Map<String, String>>()
            val firstName = params["firstName"] ?: return@post call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "firstName is required")
            )
            val lastName = params["lastName"] ?: return@post call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "lastName is required")
            )
            val initialBalance = params["initialBalance"]?.let { BigDecimal(it) } ?: BigDecimal.ZERO
            val user = userService.createUser(firstName, lastName, initialBalance)
            call.respond(HttpStatusCode.Created, user)
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, mapOf("error" to "id is required")
            )
            runCatching { userService.findUserById(id) }
                .onSuccess { call.respond(it) }
                .onFailure { call.respond(NotFound, mapOf("error" to it.message)) }
        }
    }
}
