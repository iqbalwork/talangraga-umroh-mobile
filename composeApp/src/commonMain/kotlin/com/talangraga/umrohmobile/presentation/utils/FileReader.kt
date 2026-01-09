package com.talangraga.umrohmobile.presentation.utils

import androidx.compose.runtime.Composable

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
expect class SharedFileReader {
    suspend fun readBytes(uri: String): ByteArray?
}

// In commonMain
@Composable
expect fun rememberSharedFileReader(): SharedFileReader
