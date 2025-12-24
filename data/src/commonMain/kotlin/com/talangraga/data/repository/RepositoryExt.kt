package com.talangraga.data.repository

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
