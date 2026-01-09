package com.talangraga.umrohmobile.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(FlowPreview::class)
class ListUserViewModel(
    private val repository: Repository,
    private val session: Session,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListUserUiState>(ListUserUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Store the original full list to filter locally if needed, or trigger API search
    private var allUsers: List<UserUIData> = emptyList()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        getListUser()

        // Observe search query changes with debounce
        _searchQuery
            .debounce(300) // Wait 300ms after last character
            .distinctUntilChanged()
            .onEach { query ->
                filterUsers(query)
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun filterUsers(query: String) {
        val currentAllUsers = allUsers
        if (currentAllUsers.isEmpty() && _uiState.value is ListUserUiState.Loading) {
            // Data hasn't loaded yet, do nothing or handle accordingly
            return
        }

        if (query.isBlank()) {
            _uiState.update { ListUserUiState.Success(allUsers) }
        } else {
            val filtered = allUsers.filter {
                it.fullname.contains(query, ignoreCase = true) ||
                        it.username.contains(query, ignoreCase = true)
            }
            _uiState.update {
                if (filtered.isEmpty()) ListUserUiState.EmptyData
                else ListUserUiState.Success(filtered)
            }
        }
    }

    fun clearError() {
        _errorMessage.update { null }
    }

    fun getListUser() {
        _uiState.update { ListUserUiState.Loading }
        repository.getListUsers()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                        _uiState.update { ListUserUiState.EmptyData } // Or Error state if you have one
                    }

                    is Result.Success -> {
                        val listUser = result.data.map { it.toUiData() }
                            .filter { it.id != session.userProfile.value?.id }
                        allUsers = listUser
                        // Apply current filter if any
                        filterUsers(_searchQuery.value)
                    }
                }
            }.launchIn(viewModelScope)
    }

}
