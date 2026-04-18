@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.talangraga.umrohmobile.presentation.utils

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.refTo
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
actual class ImageCompressor actual constructor() {
    actual suspend fun compress(byteArray: ByteArray, maxSizeBytes: Long): ByteArray {
        val nsData = byteArray.toNSData()
        val image = UIImage.imageWithData(nsData) ?: return byteArray
        
        var quality = 0.9
        var compressedData = UIImageJPEGRepresentation(image, quality)
        
        while ((compressedData?.length?.toLong() ?: 0L) > maxSizeBytes && quality > 0.1) {
            quality -= 0.1
            compressedData = UIImageJPEGRepresentation(image, quality)
        }
        
        // If still too big, resize
        if ((compressedData?.length?.toLong() ?: 0L) > maxSizeBytes) {
            var scale = 0.8
            while ((compressedData?.length?.toLong() ?: 0L) > maxSizeBytes && scale > 0.1) {
                val newSize = image.size.useContents {
                    CGSizeMake(width * scale, height * scale)
                }
                
                UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
                image.drawInRect(newSize.useContents {
                    CGRectMake(0.0, 0.0, width, height)
                })
                val resizedImage = UIGraphicsGetImageFromCurrentImageContext()
                UIGraphicsEndImageContext()
                
                if (resizedImage != null) {
                    compressedData = UIImageJPEGRepresentation(resizedImage, quality)
                }
                scale -= 0.2
            }
        }
        
        return compressedData?.toByteArray() ?: byteArray
    }

    private fun ByteArray.toNSData(): NSData = usePinned {
        NSData.create(bytes = it.addressOf(0), length = size.toULong())
    }

    private fun NSData.toByteArray(): ByteArray {
        val length = this.length.toInt()
        val bytes = ByteArray(length)
        if (length > 0) {
            memcpy(bytes.refTo(0), this.bytes, this.length)
        }
        return bytes
    }
}
