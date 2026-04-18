@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.talangraga.umrohmobile.presentation.utils

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
expect class ImageCompressor() {
    suspend fun compress(byteArray: ByteArray, maxSizeBytes: Long): ByteArray
}
