package com.talangraga.umrohmobile.presentation.user.editprofile

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

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
class EditProfileViewModel(
    private val repository: Repository,
    private val session: Session
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState: StateFlow<EditProfileState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<EditProfileEffect>()
    val effect: SharedFlow<EditProfileEffect> = _effect.asSharedFlow()

    init {
        val isMember = session.userProfile.value?.userType?.lowercase() != "admin"
        _uiState.update { it.copy(isMember = isMember) }
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.InitScope -> {
                _uiState.update { it.copy(userId = event.userId, isLoginUser = event.isLoginUser) }
                getUser(event.userId)
            }
            is EditProfileEvent.OnUsernameChange -> _uiState.update { it.copy(username = event.value) }
            is EditProfileEvent.OnFullnameChange -> _uiState.update { it.copy(fullname = event.value) }
            is EditProfileEvent.OnPhoneNumberChange -> _uiState.update { it.copy(phoneNumber = event.value) }
            is EditProfileEvent.OnEmailChange -> _uiState.update { it.copy(email = event.value) }
            is EditProfileEvent.OnDomicileChange -> _uiState.update { it.copy(domicile = event.value) }
            is EditProfileEvent.OnImageChange -> _uiState.update { it.copy(imageUrl = event.value) }
            is EditProfileEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
            is EditProfileEvent.SaveProfile -> saveProfile()
        }
    }

    private fun saveProfile() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }
        if (state.isLoginUser) {
            repository.updateMe(
                fullname = state.fullname,
                username = state.username,
                email = state.email,
                phone = state.phoneNumber,
                domicile = state.domicile,
                userType = state.user?.userType?.lowercase() ?: "member",
                password = "",
                imageProfile = null
            ).onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.t.message) }
                    }
                    is Result.Success -> {
                        repository.getLoginProfile()
                            .onEach {
                                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                            }.launchIn(viewModelScope)
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
                password = "",
                domicile = state.domicile,
                userType = state.user?.userType ?: "member",
                imageProfile = null
            ).onEach { result ->
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
    }

    private fun getUser(userId: String) {
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
                                username = data.username,
                                fullname = data.fullname,
                                phoneNumber = data.phone,
                                email = data.email,
                                domicile = data.domicile,
                                imageUrl = data.imageProfileUrl
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }
}
