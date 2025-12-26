package com.example.earntaka.Data

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install

// Initialize Supabase client with your project credentials
val supabase = createSupabaseClient(
    supabaseUrl = "https://brlsvcybuzzemgypcvwj.supabase.co",
    supabaseKey = "sb_publishable__2wWq0aCakeFE5XapmGdHg_6J2N6IkD"
) {
    // Install commonly used Supabase modules
    install(Postgrest)  // For database operations
    install(Auth)       // For authentication
    install(Storage)    // For file storage
    install(Realtime)   // For real-time subscriptions
}