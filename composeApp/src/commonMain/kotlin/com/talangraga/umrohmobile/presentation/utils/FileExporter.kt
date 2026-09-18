@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.talangraga.umrohmobile.presentation.utils

import androidx.compose.runtime.Composable

expect class FileExporter {
    fun exportAndShare(bytes: ByteArray, fileName: String): Boolean
}

@Composable
expect fun rememberFileExporter(): FileExporter
