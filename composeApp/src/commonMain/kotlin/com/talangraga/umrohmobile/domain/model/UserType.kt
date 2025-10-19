package com.talangraga.umrohmobile.domain.model

enum class UserType(val role: String) {
    USER("user"),
    ADMIN("admin");

    companion object {
        fun fromRole(role: String): UserType {
            return entries.firstOrNull { it.role == role } ?: USER
        }
    }
}