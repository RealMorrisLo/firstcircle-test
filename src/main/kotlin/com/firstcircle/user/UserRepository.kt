package com.firstcircle.user

import java.util.concurrent.ConcurrentHashMap

class UserRepository {
    private val users = ConcurrentHashMap<String, User>()

    fun createUser(firstName: String, lastName: String): User {
        val user = User(firstName = firstName, lastName = lastName)
        users[user.id.toString()] = user

        return user
    }

    fun findUserById(id: String): User? = users[id]
}
