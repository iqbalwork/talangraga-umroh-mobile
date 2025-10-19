package com.talangraga.umrohmobile.presentation.user

import com.talangraga.umrohmobile.data.local.database.model.UserEntity

sealed class ListUserUiState {
    object Loading : ListUserUiState()
    object EmptyData : ListUserUiState()
    data class Success(val users: List<UserEntity>) : ListUserUiState()
}
