package com.firstcircle.balance

import java.math.BigDecimal

data class UserBalance(
    val userId: String,
    val balance: BigDecimal = BigDecimal.ZERO
)
