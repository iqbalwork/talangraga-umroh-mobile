package com.talangraga.data.network

import com.talangraga.data.AppConfig
import com.talangraga.data.network.model.response.TokenResponse
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
class RefreshTokenHandler(
    private val engine: HttpClientEngine,
    private val tokenManager: TokenManager,
) {

    suspend fun getRefreshToken(refreshToken: String): BearerTokens? {
        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }

        val url = "${AppConfig.BASE_URL}auth/v1/token?grant_type=refresh_token"
        val response = httpClient.post(url) {
            header("apikey", AppConfig.SUPABASE_ANON_KEY)
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("refresh_token", refreshToken)
            })
        }

        if (response.status.isSuccess()) {
            val result = try {
                response.body<TokenResponse>()
            } catch (e: Exception) {
                Napier.e("❌ Error parsing refresh token response: ${e.message}")
                null
            }
            val newAccessToken = result?.accessToken
            val newRefreshToken = result?.refreshToken ?: refreshToken

            if (!newAccessToken.isNullOrBlank()) {
                tokenManager.saveAccessToken(newAccessToken)
                tokenManager.saveRefreshToken(newRefreshToken)
                Napier.i("✅ Token refreshed successfully via Supabase")
                return BearerTokens(accessToken = newAccessToken, refreshToken = newRefreshToken)
            } else {
                Napier.e("❌ Failed to parse new access token from Supabase")
                tokenManager.logout()
                return null
            }
        } else {
            Napier.e("❌ Refresh request failed: ${response.status}")
            tokenManager.logout()
            return null
        }

    }

}
