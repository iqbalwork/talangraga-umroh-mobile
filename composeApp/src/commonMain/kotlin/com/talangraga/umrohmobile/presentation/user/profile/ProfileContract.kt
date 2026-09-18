package com.talangraga.umrohmobile.presentation.user.profile

data class ProfileState(
    val imageUrl: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ProfileEvent {
    data class OnImageChange(val uri: String) : ProfileEvent
    data object ClearSession : ProfileEvent
    data object FetchProfile : ProfileEvent
}

sealed interface ProfileEffect {
}
