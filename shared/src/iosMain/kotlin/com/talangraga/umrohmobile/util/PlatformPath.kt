package com.talangraga.umrohmobile.util

import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.temporaryDirectory

actual fun getDataStoreDirectoryPath(): String {
    val fileManager = NSFileManager.defaultManager
    val appSupportDir = NSSearchPathForDirectoriesInDomains(
        NSApplicationSupportDirectory,
        NSUserDomainMask,
        true
    ).firstOrNull() as? String

    val appSpecificDirectory = appSupportDir?.let { "$it/YourAppName" } // Replace YourAppName

    // Create the directory if it doesn't exist
    appSpecificDirectory?.let {
        if (!fileManager.fileExistsAtPath(it)) {
            try {
                fileManager.createDirectoryAtPath(it, true, null, null)
            }
            catch (e: Exception) {
                // Handle directory creation error, e.g., log it or fallback
                println("Error creating directory: $it, error: $e")
                return fileManager.temporaryDirectory.path ?: "." // Fallback
            }
        }
        return it
    }

    // Fallback if appSupportDir is null
    return fileManager.temporaryDirectory.path ?: "."
}
