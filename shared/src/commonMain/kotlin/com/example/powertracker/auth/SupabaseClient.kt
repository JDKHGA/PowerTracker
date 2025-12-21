package com.example.powertracker.auth

import com.example.powertracker.SUPABASE_ANON_KEY
import com.example.powertracker.SUPABASE_URL
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = SUPABASE_URL,
    supabaseKey = SUPABASE_ANON_KEY
) {
    install(Auth) {
        // This enables session persistence automatically
        // It will save the session to the platform's secure storage
    }
    install(Postgrest)
}
