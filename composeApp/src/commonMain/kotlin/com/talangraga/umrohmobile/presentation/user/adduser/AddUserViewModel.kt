package com.talangraga.umrohmobile.presentation.user.adduser

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.session.Session
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class AddUserViewModel(
    private val session: Session,
    private val repository: Repository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    private val _user = MutableStateFlow<UserUIData?>(null)
    val user = _user.asStateFlow()

    val userId = mutableStateOf(0)
    val isLoginUser = mutableStateOf(false)
    val isEdit = mutableStateOf(false)
    var fullname = mutableStateOf("")
    var username = mutableStateOf("")
    var phoneNumber = mutableStateOf("")
    var email = mutableStateOf("")
    var domicile = mutableStateOf("")
    var userType = mutableStateOf("Member")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")
    val imageUri = mutableStateOf<ByteArray?>(null)

    fun onImageChange(bytes: ByteArray) {
        imageUri.value = bytes
    }

    fun onFullnameChange(newValue: String) {
        fullname.value = newValue
    }

    fun onUsernameChange(newValue: String) {
        username.value = newValue
    }

    fun onPhoneNumberChange(newValue: String) {
        phoneNumber.value = newValue
    }

    fun onEmailChange(newValue: String) {
        email.value = newValue
    }

    fun onDomicileChange(newValue: String) {
        domicile.value = newValue
    }

    fun onUserTypeChange(newValue: String) {
        userType.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        password.value = newValue
    }

    fun onConfirmPasswordChange(newValue: String) {
        confirmPassword.value = newValue
    }

    fun clearError() {
        _errorMessage.update { null }
    }

    fun saveUser() {
        if (!isEdit.value) {
            registerUser()
        } else {
            updateUser()
        }
    }

    private fun updateUser() {
        _isLoading.update { true }
        if (isLoginUser.value) {
            repository.updateMe(
                fullname = fullname.value,
                username = username.value,
                phone = phoneNumber.value,
                email = email.value,
                domicile = domicile.value,
                userType = userType.value.lowercase(),
                password = password.value,
                imageProfile = imageUri.value
            ).onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _isLoading.update { false }
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        getUserProfile()
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            repository.updateUser(
                userId = userId.value,
                fullname = fullname.value,
                username = username.value,
                email = email.value,
                phone = phoneNumber.value,
                password = password.value,
                domicile = domicile.value,
                userType = userType.value,
                imageProfile = if (imageUri.value != null) imageUri.value else null,
            ).onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _isLoading.update { false }
                        _errorMessage.update { result.t.message }
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
                        _isLoading.update { false }
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        _isLoading.update { false }
                        _isSuccess.update { true }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun fetchListUser() {
        repository.getListUsers()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _isLoading.update { false }
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        _isLoading.update { false }
                        _isSuccess.update { true }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun registerUser() {
        // Validation Logic here
        if (password.value != confirmPassword.value) {
            _errorMessage.update { "Password does not match" }
            return
        }

        _isLoading.update { true }
        repository.registerNewUser(
            fullname = fullname.value,
            username = username.value,
            phone = phoneNumber.value,
            email = email.value,
            domicile = domicile.value,
            userType = userType.value.lowercase(),
            password = password.value,
            imageProfile = imageUri.value
        ).onEach { result ->
            when (result) {
                is Result.Error -> {
                    _isLoading.update { false }
                    _errorMessage.update { result.t.message }
                }

                is Result.Success -> {
                    fetchListUser()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUser(userId: Int) {
        if (isLoginUser.value) {
            val data = session.userProfile.value?.toUiData()
            data?.let {
                onFullnameChange(data.fullname)
                onUsernameChange(data.username)
                onPhoneNumberChange(data.phone)
                onEmailChange(data.email)
                onDomicileChange(data.domicile)
                onUserTypeChange(data.userType)
            }
            _user.update { data }
        } else {
            repository.getUser(userId)
                .onEach { result ->
                    when (result) {
                        is Result.Error -> {
                            _errorMessage.update { result.t.message }
                        }

                        is Result.Success -> {
                            val data = result.data.toUiData()
                            onFullnameChange(data.fullname)
                            onUsernameChange(data.username)
                            onPhoneNumberChange(data.phone)
                            onEmailChange(data.email)
                            onDomicileChange(data.domicile)
                            onUserTypeChange(data.userType)
                            _user.update { result.data.toUiData() }
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }
}
