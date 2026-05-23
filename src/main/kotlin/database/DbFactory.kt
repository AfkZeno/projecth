package com.backend.database

import com.backend.data.datasource.Chapters
import com.backend.data.datasource.Mangas
import com.backend.data.datasource.Pages
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction


object DbFactory {
    suspend fun initDb(db: R2dbcDatabase){
        suspendTransaction(db = db) {
            if(isDevEnv()) {
                addLogger(StdOutSqlLogger)
                println("Logger añadido")
            }
            SchemaUtils.create(
                Mangas,
                Chapters,
                Pages
            )
        }
        println("Tablas creadas o verificadas correctamente")
    }

    private fun isDevEnv(): Boolean {
        val env = System.getenv("ENV") ?: "production"
        return env == "local"
    }

    suspend fun command(command: String) {
        suspendTransaction {
            exec(command)
        }
    }

}