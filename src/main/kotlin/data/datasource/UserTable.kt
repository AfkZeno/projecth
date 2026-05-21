package com.backend.data.datasource

import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable
import org.jetbrains.exposed.v1.javatime.timestamp

object UserTable : UIntIdTable("users") {
    val email = varchar("email", 255)
    val name = varchar("name", 255)
    val createdAt = timestamp("createdAt")
}