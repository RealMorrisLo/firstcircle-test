package com.firstcircle.balance

import com.firstcircle.serializer.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

@Serializable
data class UserBalance(
    val userId: String,
    @Serializable(with = Serializer.BigDecimalSerializer::class)
    val balance: BigDecimal = BigDecimal.ZERO
)
