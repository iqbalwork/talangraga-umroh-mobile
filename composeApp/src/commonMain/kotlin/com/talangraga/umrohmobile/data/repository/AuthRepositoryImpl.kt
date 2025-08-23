package com.talangraga.umrohmobile.data.repository

import SessionStore
import com.talangraga.umrohmobile.data.local.DataStoreKey
import com.talangraga.umrohmobile.data.local.saveBoolean
import com.talangraga.umrohmobile.data.local.saveString
import com.talangraga.umrohmobile.data.network.api.ApiResponse
import com.talangraga.umrohmobile.data.network.api.AuthService
import com.talangraga.umrohmobile.data.network.model.response.AuthResponse
import com.talangraga.umrohmobile.data.network.model.response.ErrorResponse
import com.talangraga.umrohmobile.data.network.model.response.StrapiError
import com.talangraga.umrohmobile.data.network.model.response.UserResponse
import com.talangraga.umrohmobile.domain.repository.AuthRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val json: Json,
    private val sessionStore: SessionStore
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
            val authData = authService.login(identifier, password)
            sessionStore.saveBoolean(DataStoreKey.IS_LOGGED_IN, true)
            sessionStore.saveString(DataStoreKey.TOKEN_KEY, authData.jwt)
            authData
        }
    }

    override fun getLoginProfile(): Flow<ApiResponse<UserResponse, ErrorResponse>> {
        return safeApiCall {
            val apiResponse = authService.getLoginProfile()
            val profileToString = json.encodeToString(UserResponse.serializer(), apiResponse)
            sessionStore.saveString(DataStoreKey.PROFILE_KEY, profileToString)
            apiResponse
        }
    }
}
