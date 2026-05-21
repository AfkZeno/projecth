package com.backend.presentation.routes

import com.backend.domain.service.UserService
import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.userRoute(){
    val service by inject<UserService>()

    routing {
        post("/"){

        }
    }
}