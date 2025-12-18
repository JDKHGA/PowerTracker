package com.example.powertracker

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

// Initialize Supabase client using environment variables for security
val supabase = createSupabaseClient(
    supabaseUrl = System.getenv("SUPABASE_URL") ?: "",
    supabaseKey = System.getenv("SUPABASE_KEY") ?: ""
) {
    install(Auth)
    install(Postgrest)
}

fun Application.module() {
    // Configure JSON serialization
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    routing {
        get("/") {
            call.respondText("PowerTracker Server is running!")
        }
        
        // Example route to check connectivity
        get("/status") {
            val isInitialized = System.getenv("SUPABASE_URL") != null && System.getenv("SUPABASE_KEY") != null
            call.respond(mapOf(
                "status" to "ok", 
                "supabase_initialized" to isInitialized
            ))
        }
    }
}
