package com.talangraga.talangragaumrohmobile.data.repository

import com.talangraga.talangragaumrohmobile.data.local.loginState
import com.talangraga.talangragaumrohmobile.data.local.tokenStore
import com.talangraga.talangragaumrohmobile.data.network.ApiResponse
import com.talangraga.talangragaumrohmobile.data.network.AuthService
import com.talangraga.talangragaumrohmobile.data.network.model.response.AuthResponse
import com.talangraga.talangragaumrohmobile.data.network.model.response.ErrorResponse
import com.talangraga.talangragaumrohmobile.data.network.model.response.StrapiError
import com.talangraga.talangragaumrohmobile.domain.repository.AuthRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val json: Json
) : AuthRepository {

    private inline fun <reified T> safeApiCall(crossinline apiCall: suspend () -> T): Flow<ApiResponse<T, ErrorResponse>> {
        return flow {
            try {
                val result = apiCall()
                emit(ApiResponse.Success(result))
            } catch (e: ClientRequestException) {
                val httpResponse: HttpResponse = e.response
                try {
                    // Attempt to parse the error body into ErrorResponse
                    val errorBody = httpResponse.body<String>()
                    val errorResponse = json.decodeFromString<ErrorResponse>(errorBody)
                    emit(ApiResponse.Error(errorResponse))
                } catch (parseException: Exception) {
                    // Fallback if parsing the error body fails or if it's not a JSON error
                    emit(
                        ApiResponse.Error(
                            ErrorResponse(
                                error = StrapiError(
                                    status = httpResponse.status.value,
                                    name = "ClientRequestError",
                                    message = parseException.message
                                        ?: "Failed to parse error body",
                                )
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                // Catch other exceptions (e.g., network issues, serialization issues not from ClientRequestException)
                emit(
                    ApiResponse.Error(
                        ErrorResponse(
                            error = StrapiError(
                                status = 0, // Or a specific status code for general errors
                                name = "GenericError",
                                message = e.message ?: "An unexpected error occurred",
                            )
                        )
                    )
                )
            }
        }
    }

    override fun login(
        identifier: String,
        password: String
    ): Flow<ApiResponse<AuthResponse, ErrorResponse>> {
        return safeApiCall {
            val authService = authService.login(identifier, password)
            loginState.set(true)
            tokenStore.set(authService.jwt)
            authService
        }
    }
}
