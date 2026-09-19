package com.talangraga.umrohmobile.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FileExporter {
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun exportAndShare(bytes: ByteArray, fileName: String): Boolean {
        return try {
            val tempDir = NSTemporaryDirectory()
            val filePath = "$tempDir$fileName"
            val nsData = bytes.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
            }
            nsData.writeToFile(filePath, true)

            val fileUrl = NSURL.fileURLWithPath(filePath)
            val activityViewController = UIActivityViewController(
                activityItems = listOf(fileUrl),
                applicationActivities = null
            )

            val currentViewController = getRootViewController()
            currentViewController?.presentViewController(
                activityViewController,
                animated = true,
                completion = null
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getRootViewController(): UIViewController? {
        val window = UIApplication.sharedApplication.windows.firstOrNull {
            (it as? UIWindow)?.isKeyWindow() == true
        } as? UIWindow ?: UIApplication.sharedApplication.keyWindow
        var rootViewController = window?.rootViewController
        while (rootViewController?.presentedViewController != null) {
            rootViewController = rootViewController.presentedViewController
        }
        return rootViewController
    }
}

@Composable
actual fun rememberFileExporter(): FileExporter {
    return remember { FileExporter() }
}
