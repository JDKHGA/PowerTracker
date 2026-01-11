package com.example.powertracker.auth

import com.example.powertracker.SupabaseConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = SupabaseConfig.URL,
    supabaseKey = SupabaseConfig.KEY
) {
    install(Auth) {
        // This enables session persistence automatically
        // It will save the session to the platform's secure storage
    }
    install(Postgrest)
    install(Functions)
}
