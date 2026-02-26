package com.firstcircle.user

import com.firstcircle.serializer.Serializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    @Serializable(with = Serializer.UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String
)
