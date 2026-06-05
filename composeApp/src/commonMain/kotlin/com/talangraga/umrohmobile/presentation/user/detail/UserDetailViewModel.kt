package com.talangraga.umrohmobile.presentation.user.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class UserDetailViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserDetailState())
    val uiState: StateFlow<UserDetailState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<UserDetailEffect>()
    val effect: SharedFlow<UserDetailEffect> = _effect.asSharedFlow()

    fun onEvent(event: UserDetailEvent) {
        when (event) {
            is UserDetailEvent.GetUser -> getUser(event.userId)
            UserDetailEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun getUser(userId: Int) {
        _uiState.update { it.copy(isLoading = true) }
        repository.getUser(userId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.t.message
                            )
                        }
                    }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                user = result.data.toUiData()
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }
}
