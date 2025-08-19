package com.talangraga.umrohmobile.repository

import com.talangraga.umrohmobile.data.model.AuthResponse
import com.talangraga.umrohmobile.data.model.StrapiErrorResponse
import com.talangraga.umrohmobile.data.network.AuthService
import com.talangraga.umrohmobile.domain.Result
import com.talangraga.umrohmobile.domain.RootError
// import com.talangraga.umrohmobile.session.SessionManager // Remove this
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) {

    private fun <T> executeAuthRequest(block: suspend () -> T): Flow<Result<T>> = flow {
        emit(Result.Loading)
        try {
            val response = block()
            emit(Result.Success(response))
        } catch (e: ClientRequestException) {
            val errorResponse: StrapiErrorResponse? = try {
                e.response.body()
            } catch (bodyException: Exception) {
                null
            }
            emit(Result.Error(RootError.ServerError(errorResponse?.error?.message)))
        } catch (e: Exception) {
            emit(Result.Error(RootError.UnknownError(e)))
        }
    }

    fun loginUser(identifier: String, password: String): Flow<Result<AuthResponse>> =
        executeAuthRequest {
            authService.login(identifier, password)
        }.map {
            if (it is Result.Success) {
                it.data.jwt.let { token -> tokenManager.saveToken(token) } // Use tokenRepository
            }
            it
        }

    suspend fun registerNewUser(
        username: String,
        email: String,
        password: String
    ): AuthResponse {
        val response = authService.registerUser(username, email, password)
        response.jwt?.let { tokenManager.saveToken(it) } // Use tokenRepository
        return response
    }
}
