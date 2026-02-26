package com.firstcircle.user

import com.firstcircle.serializer.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

@Serializable
data class User(
    @Serializable(with = Serializer.UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String
)
