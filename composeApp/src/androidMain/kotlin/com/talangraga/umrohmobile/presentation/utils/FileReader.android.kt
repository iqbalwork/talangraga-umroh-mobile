package com.talangraga.umrohmobile.presentation.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class SharedFileReader(private val context: Context) {
    actual suspend fun readBytes(uri: String): ByteArray? = withContext(Dispatchers.IO) {
        try {
            val contentUri = uri.toUri()
            context.contentResolver.openInputStream(contentUri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Composable
actual fun rememberSharedFileReader(): SharedFileReader {
    val context = LocalContext.current
    return remember(context) { SharedFileReader(context) }
}
