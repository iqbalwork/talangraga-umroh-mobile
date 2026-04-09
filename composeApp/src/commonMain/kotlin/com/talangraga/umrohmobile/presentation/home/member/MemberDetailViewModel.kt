package com.talangraga.umrohmobile.presentation.home.member

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

class MemberDetailViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemberDetailState())
    val uiState: StateFlow<MemberDetailState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<MemberDetailEffect>()
    val effect: SharedFlow<MemberDetailEffect> = _effect.asSharedFlow()

    fun onEvent(event: MemberDetailEvent) {
        when (event) {
            is MemberDetailEvent.GetUser -> getUser(event.userId)
        }
    }

    private fun getUser(userId: Int) {
        repository.getUser(userId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val message = result.t.message
                        _uiState.update { it.copy(errorMessage = message) }
                    }

                    is Result.Success -> {
                        val data = result.data.toUiData()
                        _uiState.update { it.copy(user = data) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
