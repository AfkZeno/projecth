package com.backend.domain.repository

import com.backend.domain.model.User

interface UserRepository {
    suspend fun create(user: User): User
    suspend fun findById(id: Long): User?
    suspend fun findByEmail(email: String): User?
}