@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.talangraga.umrohmobile.presentation.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
actual class ImageCompressor actual constructor() {
    actual suspend fun compress(byteArray: ByteArray, maxSizeBytes: Long): ByteArray {
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options) ?: return byteArray
        
        var quality = 90
        var outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        
        var result = outputStream.toByteArray()
        
        // If still too big, try reducing quality
        while (result.size > maxSizeBytes && quality > 10) {
            quality -= 10
            outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            result = outputStream.toByteArray()
        }
        
        // If still too big, resize dimensions
        if (result.size > maxSizeBytes) {
            var scale = 0.8f
            while (result.size > maxSizeBytes && scale > 0.1f) {
                val width = (bitmap.width * scale).toInt()
                val height = (bitmap.height * scale).toInt()
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
                
                outputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                result = outputStream.toByteArray()
                scale -= 0.2f
            }
        }
        
        return result
    }
}
