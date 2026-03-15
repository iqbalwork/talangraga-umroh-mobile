package com.talangraga.umrohmobile.presentation.user.changepassword

data class ChangePasswordState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isPasswordChanged: Boolean = false
)

sealed interface ChangePasswordEvent {
    data class OnCurrentPasswordChange(val password: String) : ChangePasswordEvent
    data class OnNewPasswordChange(val password: String) : ChangePasswordEvent
    data class OnConfirmPasswordChange(val password: String) : ChangePasswordEvent
    data object ClearError : ChangePasswordEvent
    data object ChangePassword : ChangePasswordEvent
}

sealed interface ChangePasswordEffect {
    data class ShowToastError(val message: String) : ChangePasswordEffect
}
