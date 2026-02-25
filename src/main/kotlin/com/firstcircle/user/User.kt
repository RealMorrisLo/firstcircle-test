package com.firstcircle.user

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String
)