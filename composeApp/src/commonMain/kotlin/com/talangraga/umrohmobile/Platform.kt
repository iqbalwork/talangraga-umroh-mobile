package com.talangraga.umrohmobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform