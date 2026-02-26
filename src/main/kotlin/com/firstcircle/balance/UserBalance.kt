package com.firstcircle.balance

import com.firstcircle.serializer.Serializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class UserBalance(
    val userId: String,
    @Serializable(with = Serializer.BigDecimalSerializer::class)
    val balance: BigDecimal = BigDecimal.ZERO
)
