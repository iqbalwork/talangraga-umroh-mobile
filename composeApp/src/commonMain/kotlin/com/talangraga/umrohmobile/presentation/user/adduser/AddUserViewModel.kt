package com.talangraga.umrohmobile.presentation.user.adduser

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddUserViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    var fullname = mutableStateOf("")
    var username = mutableStateOf("")
    var phoneNumber = mutableStateOf("")
    var email = mutableStateOf("")
    var domicile = mutableStateOf("")
    var userType = mutableStateOf("Member")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

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
        // Validation Logic here
        if (password.value != confirmPassword.value) {
            _errorMessage.update { "Password does not match" }
            return
        }
        
        // Call repository to save user
        _isLoading.update { true }
        // Simulate API call
    }
}
