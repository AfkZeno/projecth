package com.backend.data.repository

import com.backend.domain.model.User
import com.backend.domain.repository.UserRepository
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

class UserRepositoryImpl(private val db: R2dbcDatabase) : UserRepository {
    override suspend fun create(user: User): User {
        TODO("Not yet implemented")
    }

    override suspend fun findByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: Long): User? {
        TODO("Not yet implemented")
    }
}