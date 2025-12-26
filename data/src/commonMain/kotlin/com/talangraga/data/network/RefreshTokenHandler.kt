package com.talangraga.data.network

import com.talangraga.data.BuildKonfig
import com.talangraga.data.network.model.response.TokenResponse
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

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

        val response = httpClient.post("${BuildKonfig.BASE_URL}auth/refresh") {
            header(HttpHeaders.Authorization, "Bearer $refreshToken")
        }

        if (response.status.isSuccess()) {
            val result = response.body<TokenResponse>()
            val newAccessToken = result.accessToken

            if (!newAccessToken.isNullOrBlank()) {
                tokenManager.saveAccessToken(newAccessToken)
                Napier.i("✅ Token refreshed successfully")
                return BearerTokens(accessToken = newAccessToken, refreshToken = refreshToken)
            } else {
                Napier.e("❌ Failed to parse new access token")
                return null
            }
        } else {
            Napier.e("❌ Refresh request failed: ${response.status}")
            return null
        }

    }

}
