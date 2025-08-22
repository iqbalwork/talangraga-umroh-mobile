package com.talangraga.talangragaumrohmobile.module

import com.talangraga.talangragaumrohmobile.data.local.files
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformModule: Module
    get() = module {
        single {

            val fileManager: NSFileManager = NSFileManager.defaultManager
            val documentsUrl: NSURL? = fileManager.URLForDirectory(
                directory = NSDocumentDirectory,
                appropriateForURL = null,
                create = false,
                inDomain = NSUserDomainMask,
                error = null
            )

            files = Path(documentsUrl?.path.orEmpty())
        }
    }