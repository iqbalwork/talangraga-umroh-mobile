package com.talangraga.umrohmobile.presentation.user.adduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
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

class AddUserViewModel(
    private val session: Session,
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddUserState())
    val uiState: StateFlow<AddUserState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AddUserEffect>()
    val effect: SharedFlow<AddUserEffect> = _effect.asSharedFlow()

    fun onEvent(event: AddUserEvent) {
        when (event) {
            is AddUserEvent.InitScope -> {
                _uiState.update { it.copy(userId = event.userId, isLoginUser = event.isLoginUser, isEdit = event.isEdit) }
                if (event.userId > 0) getUser(event.userId)
            }
            is AddUserEvent.OnImageChange -> _uiState.update { it.copy(imageUri = event.bytes) }
            is AddUserEvent.OnFullnameChange -> _uiState.update { it.copy(fullname = event.newValue) }
            is AddUserEvent.OnUsernameChange -> _uiState.update { it.copy(username = event.newValue) }
            is AddUserEvent.OnPhoneNumberChange -> _uiState.update { it.copy(phoneNumber = event.newValue) }
            is AddUserEvent.OnEmailChange -> _uiState.update { it.copy(email = event.newValue) }
            is AddUserEvent.OnDomicileChange -> _uiState.update { it.copy(domicile = event.newValue) }
            is AddUserEvent.OnUserTypeChange -> _uiState.update { it.copy(userType = event.newValue) }
            is AddUserEvent.OnPasswordChange -> _uiState.update { it.copy(password = event.newValue) }
            is AddUserEvent.OnConfirmPasswordChange -> _uiState.update { it.copy(confirmPassword = event.newValue) }
            is AddUserEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
            is AddUserEvent.SaveUser -> saveUser()
        }
    }

    private fun saveUser() {
        if (!_uiState.value.isEdit) {
            registerUser()
        } else {
            updateUser()
        }
    }

    private fun updateUser() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }
        if (state.isLoginUser) {
            repository.updateMe(
                fullname = state.fullname,
                username = state.username,
                phone = state.phoneNumber,
                email = state.email,
                domicile = state.domicile,
                userType = state.userType.lowercase(),
                password = state.password,
                imageProfile = state.imageUri
            ).onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.t.message) }
                    }

                    is Result.Success -> {
                        getUserProfile()
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            repository.updateUser(
                userId = state.userId,
                fullname = state.fullname,
                username = state.username,
                email = state.email,
                phone = state.phoneNumber,
                password = state.password,
                domicile = state.domicile,
                userType = state.userType,
                imageProfile = state.imageUri
            ).onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.t.message) }
                    }

                    is Result.Success -> {
                        fetchListUser()
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getUserProfile() {
        repository.getLoginProfile()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.t.message) }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun fetchListUser() {
        repository.getListUsers()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.t.message) }
                    }

                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun registerUser() {
        val state = _uiState.value
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Password does not match") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        repository.registerNewUser(
            fullname = state.fullname,
            username = state.username,
            phone = state.phoneNumber,
            email = state.email,
            domicile = state.domicile,
            userType = state.userType.lowercase(),
            password = state.password,
            imageProfile = state.imageUri
        ).onEach { result ->
            when (result) {
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.t.message) }
                }

                is Result.Success -> {
                    fetchListUser()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getUser(userId: Int) {
        val state = _uiState.value
        if (state.isLoginUser) {
            val data = session.userProfile.value?.toUiData()
            data?.let {
                _uiState.update {
                    it.copy(
                        user = data,
                        fullname = data.fullname,
                        username = data.username,
                        phoneNumber = data.phone,
                        email = data.email,
                        domicile = data.domicile,
                        userType = data.userType
                    )
                }
            }
        } else {
            repository.getUser(userId)
                .onEach { result ->
                    when (result) {
                        is Result.Error -> {
                            _uiState.update { it.copy(errorMessage = result.t.message) }
                        }

                        is Result.Success -> {
                            val data = result.data.toUiData()
                            _uiState.update {
                                it.copy(
                                    user = data,
                                    fullname = data.fullname,
                                    username = data.username,
                                    phoneNumber = data.phone,
                                    email = data.email,
                                    domicile = data.domicile,
                                    userType = data.userType
                                )
                            }
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }
}
