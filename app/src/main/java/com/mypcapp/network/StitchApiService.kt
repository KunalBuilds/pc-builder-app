package com.mypcapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// ─── Gemini API ───────────────────────────────────────────────────────────────

interface GeminiApiService {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

data class GeminiRequest(val contents: List<GeminiContent>)
data class GeminiContent(val parts: List<GeminiPart>, val role: String = "user")
data class GeminiPart(val text: String)

data class GeminiResponse(val candidates: List<GeminiCandidate>?)
data class GeminiCandidate(val content: GeminiContent?)

// ─── Stitch API (kept for backwards-compat) ───────────────────────────────────

interface StitchApiService {
    @retrofit2.http.POST("v1/chat/completions")
    suspend fun sendMessage(
        @retrofit2.http.Header("Authorization") authorization: String,
        @Body request: StitchChatRequest
    ): StitchChatResponse
}

data class StitchChatRequest(
    val model: String = "stitch",
    val messages: List<StitchMessage>,
    val max_tokens: Int = 500,
    val temperature: Double = 0.7
)
data class StitchMessage(val role: String, val content: String)
data class StitchChatResponse(val choices: List<StitchChoice>)
data class StitchChoice(val message: StitchMessage, val finish_reason: String)

// ─── Network Clients ─────────────────────────────────────────────────────────

object NetworkClient {

    private const val STITCH_BASE_URL = "https://api.stitch.com/"
    private const val GEMINI_BASE_URL  = "https://generativelanguage.googleapis.com/"

    private val httpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        // Only log in debug builds
        if (com.mypcapp.BuildConfig.ENABLE_LOGGING) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }

        builder.build()
    }

    val stitchApi: StitchApiService by lazy {
        Retrofit.Builder()
            .baseUrl(STITCH_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StitchApiService::class.java)
    }

    val geminiApi: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}
