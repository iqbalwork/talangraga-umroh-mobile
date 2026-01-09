package com.talangraga.umrohmobile.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL

@OptIn(ExperimentalForeignApi::class)
actual class SharedFileReader {
    actual suspend fun readBytes(uri: String): ByteArray? {
        val nsUrl = NSURL.URLWithString(uri) ?: return null
        val data = NSData.dataWithContentsOfURL(nsUrl) ?: return null

        return if (data.length > 0u) {
            val bytes = data.bytes?.reinterpret<ByteVar>()
            bytes?.readBytes(data.length.toInt())
        } else {
            null
        }
    }
}

// In iosMain
@Composable
actual fun rememberSharedFileReader(): SharedFileReader {
    return remember { SharedFileReader() }
}
