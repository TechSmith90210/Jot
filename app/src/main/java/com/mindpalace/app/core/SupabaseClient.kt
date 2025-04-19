package com.mindpalace.app.core

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = "https://wrllztucibjqjjcpngjw.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndybGx6dHVjaWJqcWpqY3BuZ2p3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQ5OTI3NjIsImV4cCI6MjA2MDU2ODc2Mn0.8Glm94wLBAXQBfwBP5hQTMOrG5rmUKxFLsak8JTdjV4"
    ) {
        install(Auth)
        install(Postgrest)
    }

}