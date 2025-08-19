package com.talangraga.umrohmobile.data.network

import com.talangraga.umrohmobile.constant.getBaseUrl
import com.talangraga.umrohmobile.data.model.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class AuthService(private val httpClient: HttpClient) {

    @Serializable
    private data class LoginRequest(
        val identifier: String,
        val password: String
    )

    @Serializable
    private data class RegisterRequest(
        val username: String,
        val email: String,
        val password: String
    )

    private suspend inline fun <reified T> postAuthRequest(pathSegment: String, requestBody: T): AuthResponse {
        return httpClient.post("${getBaseUrl()}$pathSegment") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()
    }

    suspend fun login(identifier: String, password: String): AuthResponse {
        val loginRequest = LoginRequest(identifier, password)
        return postAuthRequest("auth/local", loginRequest)
    }

    suspend fun registerUser(username: String, email: String, password: String): AuthResponse {
        val registerRequest = RegisterRequest(username, email, password)
        return postAuthRequest("auth/local/register", registerRequest)
    }

}