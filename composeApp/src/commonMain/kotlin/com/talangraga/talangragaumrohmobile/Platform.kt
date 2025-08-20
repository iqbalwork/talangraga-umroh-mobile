package com.talangraga.talangragaumrohmobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform