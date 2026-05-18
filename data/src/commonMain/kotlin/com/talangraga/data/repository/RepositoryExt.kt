package com.talangraga.data.repository

import com.talangraga.data.network.api.Result
import com.talangraga.data.network.model.response.DataResponse
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun normalizeErrorMessage(throwable: Throwable): String {
    val message = throwable.message ?: "Unknown error"

    return when {
        message.contains("Failed to connect", ignoreCase = true) ->
            "Failed to connect to the server."

        message.contains("Could not connect", ignoreCase = true) ||
                message.contains("NSURLErrorDomain", ignoreCase = true) ||
                message.contains("Code=-1004", ignoreCase = true) ->
            "Failed to connect to the server."

        message.contains("timed out", ignoreCase = true) ->
            "Connection timed out."

        message.contains("Unauthorized", ignoreCase = true) ->
            "Unauthorized. Please check your credentials."

        message.contains("Network is unreachable", ignoreCase = true) ->
            "No internet connection."

        else -> message
    }
}

inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> DataResponse<T>,
    crossinline onSuccess: suspend (T) -> Unit = {}
): Flow<Result<T>> = flow {
    try {
        val response = apiCall()
        val data = response.data

        if (data != null) {
            onSuccess(data)
            emit(Result.Success(data))
        } else {
            emit(Result.Error(Exception(response.message)))
        }
    } catch (e: JsonConvertException) {
        val message = normalizeErrorMessage(e)
        emit(Result.Error(Exception(message)))
    } catch (e: Exception) {
        val message = normalizeErrorMessage(e)
        emit(Result.Error(Exception(message)))
    }
}

fun <LocalType, NetworkType> networkBoundResource(
    query: () -> Flow<LocalType>?,
    fetch: suspend () -> DataResponse<NetworkType>,
    saveFetchResult: suspend (LocalType) -> Unit,
    mapper: (NetworkType) -> LocalType
): Flow<Result<LocalType>> = channelFlow {

    val db = launch {
        query()?.collectLatest { data ->
            send(Result.Success(data))
        }
    }

    launch(Dispatchers.IO) {
        try {
            // Fetch new data
            val networkResponse = fetch()
            if (networkResponse.data != null) {
                val mappedData = mapper(networkResponse.data)
                // Replace cache
                saveFetchResult(mappedData)
                // Emit updated data
                send(Result.Success(mappedData))
            } else {
                send(Result.Error(Exception(networkResponse.message)))
            }
        } catch (e: JsonConvertException) {
            val message =
                normalizeErrorMessage(e)
            send(Result.Error(Exception(message)))
        } catch (e: Exception) {
            val message =
                normalizeErrorMessage(e)
            send(Result.Error(Exception(message)))
        }
    }

    awaitClose { db.cancel() }
}
