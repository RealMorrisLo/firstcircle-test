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

class UserBalanceRoutesTest : FunSpec({

    context("happy flow") {
        test("should get balance for existing user then return 200 with balance") {
            testApplication {
                application { module() }
                val createResponse = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"firstName":"Alice","lastName":"Wong","initialBalance":"500.00"}""")
                }
                createResponse.status shouldBe HttpStatusCode.Created
                val userId = Json.parseToJsonElement(
                    createResponse.bodyAsText()
                ).jsonObject["id"]!!.jsonPrimitive.content

                val response = client.get("/balances/$userId")
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldContain "500"
            }
        }

        test("should deposit amount then return 200 with updated balance") {
            testApplication {
                application { module() }
                val createResponse = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"firstName":"Bob","lastName":"Lee","initialBalance":"100.00"}""")
                }
                val userId = Json.parseToJsonElement(
                    createResponse.bodyAsText()
                ).jsonObject["id"]!!.jsonPrimitive.content

                val response = client.post("/balances/deposit") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"userId":"$userId","amount":"50.00"}""")
                }
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldContain "150"
            }
        }

        test("should withdraw amount then return 200 with updated balance") {
            testApplication {
                application { module() }
                val createResponse = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"firstName":"Carol","lastName":"Tan","initialBalance":"200.00"}""")
                }
                val userId = Json.parseToJsonElement(
                    createResponse.bodyAsText()
                ).jsonObject["id"]!!.jsonPrimitive.content

                val response = client.post("/balances/withdraw") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"userId":"$userId","amount":"80.00"}""")
                }
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldContain "120"
            }
        }

        test("should transfer amount between users then return 204") {
            testApplication {
                application { module() }
                val fromResponse = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"firstName":"Eve","lastName":"Park","initialBalance":"300.00"}""")
                }
                val fromUserId = Json.parseToJsonElement(
                    fromResponse.bodyAsText()
                ).jsonObject["id"]!!.jsonPrimitive.content

                val toResponse = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"firstName":"Frank","lastName":"Kim","initialBalance":"100.00"}""")
                }
                val toUserId = Json.parseToJsonElement(toResponse.bodyAsText()).jsonObject["id"]!!.jsonPrimitive.content

                val response = client.post("/balances/transfer") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"fromUserId":"$fromUserId","toUserId":"$toUserId","amount":"100.00"}""")
                }
                response.status shouldBe HttpStatusCode.NoContent
            }
        }
    }

    context("failed cases") {
        test("should get balance for non-existent user then return 400") {
            testApplication {
                application { module() }
                val response = client.get("/balances/non-existent-id")
                response.status shouldBe HttpStatusCode.BadRequest
            }
        }

        test("should deposit without userId then return 400") {
            testApplication {
                application { module() }
                val response = client.post("/balances/deposit") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"amount":"50.00"}""")
                }
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldContain "userId is required"
            }
        }

        test("should withdraw more than available balance then return 400") {
            testApplication {
                application { module() }
                val createResponse = client.post("/users") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"firstName":"Dave","lastName":"Cruz","initialBalance":"10.00"}""")
                }
                val userId = Json.parseToJsonElement(
                    createResponse.bodyAsText()
                ).jsonObject["id"]!!.jsonPrimitive.content

                val response = client.post("/balances/withdraw") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"userId":"$userId","amount":"999.00"}""")
                }
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldContain "Insufficient balance"
            }
        }

        test("should transfer without fromUserId then return 400") {
            testApplication {
                application { module() }
                val response = client.post("/balances/transfer") {
                    contentType(ContentType.Application.Json)
                    setBody("""{"toUserId":"some-id","amount":"50.00"}""")
                }
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldContain "fromUserId is required"
            }
        }
    }
})
