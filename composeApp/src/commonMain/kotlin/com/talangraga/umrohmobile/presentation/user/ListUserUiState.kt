package com.talangraga.umrohmobile.presentation.user

import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

sealed class ListUserUiState {
    object Loading : ListUserUiState()
    object EmptyData : ListUserUiState()
    data class Success(val users: List<UserUIData>) : ListUserUiState()
}
