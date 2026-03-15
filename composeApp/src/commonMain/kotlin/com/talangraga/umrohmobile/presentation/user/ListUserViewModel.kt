package com.talangraga.umrohmobile.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(FlowPreview::class)
class ListUserViewModel(
    private val repository: Repository,
    private val session: Session,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUserState())
    val uiState: StateFlow<ListUserState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ListUserEffect>()
    val effect: SharedFlow<ListUserEffect> = _effect.asSharedFlow()

    // Store the original full list to filter locally if needed, or trigger API search
    private var allUsers: List<UserUIData> = emptyList()

    private val _searchQuery = MutableStateFlow("")

    // We can also expose properties directly for convenience if still used anywhere, but in MVI they should be accessed via uiState
    val users = _uiState.map { it.users }
    val selectedUser = _uiState.map { it.selectedUser }

    init {
        onEvent(ListUserEvent.GetListUser)

        // Observe search query changes with debounce
        _searchQuery
            .debounce(300) // Wait 300ms after last character
            .distinctUntilChanged()
            .onEach { query ->
                filterUsers(query)
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: ListUserEvent) {
        when (event) {
            is ListUserEvent.GetListUser -> getListUser()
            is ListUserEvent.SearchQueryChanged -> {
                _searchQuery.value = event.query
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            is ListUserEvent.SelectUser -> {
                _uiState.update { it.copy(selectedUser = event.user) }
            }
            is ListUserEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    // Still accessible if explicitly called, though MVI discourages direct calls. For backward compatibility if other view models use it.
    fun onSelectedUser(user: UserUIData) {
        onEvent(ListUserEvent.SelectUser(user))
    }

    private fun filterUsers(query: String) {
        val currentAllUsers = allUsers
        if (currentAllUsers.isEmpty() && _uiState.value.listState is ListUserUiState.Loading) {
            // Data hasn't loaded yet, do nothing or handle accordingly
            return
        }

        if (query.isBlank()) {
            _uiState.update { it.copy(listState = ListUserUiState.Success(allUsers)) }
        } else {
            val filtered = allUsers.filter {
                it.fullname.contains(query, ignoreCase = true) ||
                        it.username.contains(query, ignoreCase = true)
            }
            _uiState.update {
                it.copy(
                    listState = if (filtered.isEmpty()) ListUserUiState.EmptyData else ListUserUiState.Success(filtered)
                )
            }
        }
    }

    private fun getListUser() {
        _uiState.update { it.copy(listState = ListUserUiState.Loading) }
        repository.getListUsers()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        val message = result.t.message
                        _uiState.update { it.copy(errorMessage = message, listState = ListUserUiState.EmptyData) } // Or Error state if you have one
                    }

                    is Result.Success -> {
                        val listUser = result.data.map { it.toUiData() }
                            .filter { it.id != session.userProfile.value?.id }
                        allUsers = listUser
                        _uiState.update { it.copy(users = result.data.map { dto -> dto.toUiData() }) }
                        // Apply current filter if any
                        filterUsers(_searchQuery.value)
                    }
                }
            }.launchIn(viewModelScope)
    }

}
