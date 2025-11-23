package com.talangraga.umrohmobile.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.umrohmobile.data.network.api.Result
import com.talangraga.umrohmobile.domain.repository.Repository
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ListUserViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListUserUiState>(ListUserUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getListUser()
    }

    fun getListUser() {
        repository.getListUsers()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            val listUser = result.data.map { it.toUiData() }
                            ListUserUiState.Success(listUser)
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

}
