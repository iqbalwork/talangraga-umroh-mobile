package com.talangraga.umrohmobile.presentation.user

import com.talangraga.umrohmobile.presentation.user.model.UserUIData

sealed class ListUserUiState {
    data object Loading : ListUserUiState()
    data class Success(val users: List<UserUIData>) : ListUserUiState()
    data object EmptyData : ListUserUiState()
}

data class ListUserState(
    val listState: ListUserUiState = ListUserUiState.Loading,
    val users: List<UserUIData> = emptyList(),
    val selectedUser: UserUIData? = null,
    val searchQuery: String = "",
    val errorMessage: String? = null
)

sealed interface ListUserEvent {
    data object GetListUser : ListUserEvent
    data class SearchQueryChanged(val query: String) : ListUserEvent
    data class SelectUser(val user: UserUIData) : ListUserEvent
    data object ClearError : ListUserEvent
}

sealed interface ListUserEffect {
    data class ShowToastError(val message: String) : ListUserEffect
}
