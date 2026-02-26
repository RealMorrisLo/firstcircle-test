package com.firstcircle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class UserRoutesTest : FunSpec({

    context("happy flow") {
        test("should create user then return 201 with user data") {
            testApplication {
                application { module() }
                val response = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"firstName":"John","lastName":"Doe","initialBalance":"100.00"}""")
                }
                response.status shouldBe HttpStatusCode.Created
                val body = response.bodyAsText()
                body shouldContain "John"
                body shouldContain "Doe"
            }
        }

        test("should get user by id then return 200 with user data") {
            testApplication {
                application { module() }
                val createResponse = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"firstName":"Jane","lastName":"Smith"}""")
                }
                createResponse.status shouldBe HttpStatusCode.Created
                val id = Json.parseToJsonElement(createResponse.bodyAsText()).jsonObject["id"]!!.jsonPrimitive.content

                val getResponse = client.get("/users/$id")
                getResponse.status shouldBe HttpStatusCode.OK
                getResponse.bodyAsText() shouldContain "Jane"
            }
        }
    }

    context("failed cases") {
        test("should create user without firstName then return 400") {
            testApplication {
                application { module() }
                val response = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"lastName":"Doe"}""")
                }
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldContain "firstName is required"
            }
        }

        test("should create user without lastName then return 400") {
            testApplication {
                application { module() }
                val response = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"firstName":"John"}""")
                }
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldContain "lastName is required"
            }
        }

        test("should get non-existent user then return 404") {
            testApplication {
                application { module() }
                val response = client.get("/users/non-existent-id")
                response.status shouldBe HttpStatusCode.NotFound
                response.bodyAsText() shouldContain "not found"
            }
        }
    }
})
