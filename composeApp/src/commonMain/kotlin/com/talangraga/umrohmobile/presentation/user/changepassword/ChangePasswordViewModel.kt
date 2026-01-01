package com.talangraga.umrohmobile.presentation.user.changepassword

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
class ChangePasswordViewModel: ViewModel() {

    private val _currentPassword = MutableStateFlow("")
    val currentPassword = _currentPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    fun onCurrentPasswordChange(password: String) {
        _currentPassword.value = password
    }

    fun onNewPasswordChange(password: String) {
        _newPassword.value = password
    }

    fun onConfirmPasswordChange(password: String) {
        _confirmPassword.value = password
    }

    fun changePassword() {

    }

}
