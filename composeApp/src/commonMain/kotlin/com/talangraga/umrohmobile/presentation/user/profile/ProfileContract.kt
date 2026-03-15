package com.talangraga.umrohmobile.presentation.user.profile

data class ProfileState(
    val imageUrl: String? = null
)

sealed interface ProfileEvent {
    data class OnImageChange(val uri: String) : ProfileEvent
    data object ClearSession : ProfileEvent
}

sealed interface ProfileEffect {
}
