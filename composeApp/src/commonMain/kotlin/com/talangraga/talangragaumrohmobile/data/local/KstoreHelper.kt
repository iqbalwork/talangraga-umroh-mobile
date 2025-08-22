package com.talangraga.talangragaumrohmobile.data.local

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path

var files: Path? = null
val tokenStore: KStore<String> = storeOf(file = Path("$files/token.json"))
val loginState: KStore<Boolean> = storeOf(file = Path("$files/loginState.json"))