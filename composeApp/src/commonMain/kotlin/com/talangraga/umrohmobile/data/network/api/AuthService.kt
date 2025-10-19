package com.talangraga.umrohmobile.data.network.api

import com.talangraga.umrohmobile.data.network.model.request.LoginRequest
import com.talangraga.umrohmobile.data.network.model.response.AuthResponse
import com.talangraga.umrohmobile.data.network.model.response.DataResponse
import com.talangraga.umrohmobile.data.network.model.response.PaymentResponse
import com.talangraga.umrohmobile.data.network.model.response.PeriodeResponse
import com.talangraga.umrohmobile.data.network.model.response.TransactionResponse
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthService(private val httpClient: HttpClient) {

    private suspend inline fun <reified T> postAuthRequest(
        pathSegment: String,
        requestBody: T
    ): AuthResponse {
        return httpClient.post(pathSegment) { // Ktor client is already configured with the base URL
            contentType(ContentType.Application.Json)
            setBody(requestBody)
            // Ktor's body() function will typically throw an exception for non-2xx responses
            // or if deserialization fails.
            // For more explicit control, HttpCallValidator with expectSuccess can be used
            // at the client level or per request via expectSuccess = true in the request block.
        }.body()
    }

    suspend fun login(identifier: String, password: String): AuthResponse {
        val loginRequest = LoginRequest(identifier = identifier, password = password)
        return httpClient.post("auth/local") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
//        return postAuthRequest("auth/local", loginRequest)
    }

    suspend fun getLoginProfile(): UserResponse {
        return httpClient.get("users/me?populate=*").body<UserResponse>()
    }

    suspend fun getListUsers(): List<UserResponse> {
        return httpClient.get("users") {
            url {
                parameters.append("populate", "*")
            }
        }.body<List<UserResponse>>()
    }

    suspend fun getPeriods(): DataResponse<List<PeriodeResponse>> {
        return httpClient.get("periodes").body<DataResponse<List<PeriodeResponse>>>()
    }

    suspend fun getPayments(): DataResponse<List<PaymentResponse>> {
        return httpClient.get("payments").body<DataResponse<List<PaymentResponse>>>()
    }

    suspend fun getTransactions(periodId: Int): DataResponse<List<TransactionResponse>> {
        return httpClient.get("transactions") {
            url {
                parameters.append("populate", "*")
                parameters.append("filters[periode][id]", periodId.toString())
            }
        }.body<DataResponse<List<TransactionResponse>>>()
    }
}