package com.talangraga.umrohmobile.data.network.api

import com.talangraga.umrohmobile.data.network.model.request.LoginRequest
import com.talangraga.umrohmobile.data.network.model.response.AuthResponse
import com.talangraga.umrohmobile.data.network.model.response.DataResponse
import com.talangraga.umrohmobile.data.network.model.response.PaymentResponse
import com.talangraga.umrohmobile.data.network.model.response.PeriodeResponse
import com.talangraga.umrohmobile.data.network.model.response.TokenResponse
import com.talangraga.umrohmobile.data.network.model.response.TransactionResponse
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService(private val httpClient: HttpClient) {

    private val authProvider = httpClient.authProvider<BearerAuthProvider>()

    suspend fun login(identifier: String, password: String): DataResponse<TokenResponse> {
        val loginRequest = LoginRequest(identifier = identifier, password = password)
        authProvider?.clearToken()
        return httpClient.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
    }

    suspend fun getLoginProfile(): DataResponse<UserResponse> {
        return httpClient.get("auth/profile").body()
    }

    suspend fun getListUsers(): DataResponse<List<UserResponse>> {
        return httpClient.get("users").body()
    }

    suspend fun getPeriods(): DataResponse<List<PeriodeResponse>> {
        return httpClient.get("periodes").body()
    }

    suspend fun getPayments(): DataResponse<List<PaymentResponse>> {
        return httpClient.get("payments").body()
    }

    suspend fun getTransactions(
        periodId: Int?,
        status: String?,
        paymentId: Int?
    ): DataResponse<List<TransactionResponse>> {
        return httpClient.get("transactions") {
            url {
                periodId?.let {
                    parameters.append("periode_id", periodId.toString())
                }
                status?.let {
                    parameters.append("status", status)
                }
                paymentId?.let {
                    parameters.append("payment_id", paymentId.toString())
                }
            }
        }.body()
    }
}
