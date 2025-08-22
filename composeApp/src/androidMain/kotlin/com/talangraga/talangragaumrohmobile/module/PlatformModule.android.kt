package com.talangraga.talangragaumrohmobile.module

import com.talangraga.talangragaumrohmobile.data.local.files
import kotlinx.io.files.Path
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single {
            files = Path(androidContext().filesDir.path)
        }
    }