package com.talangraga.umrohmobile.presentation.user.adduser

import com.talangraga.umrohmobile.presentation.user.model.UserUIData

data class AddUserState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: UserUIData? = null,
    val userId: Int = 0,
    val isLoginUser: Boolean = false,
    val isEdit: Boolean = false,
    val fullname: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val domicile: String = "",
    val userType: String = "Member",
    val password: String = "",
    val confirmPassword: String = "",
    val imageUri: ByteArray? = null
)

sealed interface AddUserEvent {
    data class InitScope(val userId: Int, val isLoginUser: Boolean, val isEdit: Boolean) : AddUserEvent
    data class OnImageChange(val bytes: ByteArray) : AddUserEvent
    data class OnFullnameChange(val newValue: String) : AddUserEvent
    data class OnUsernameChange(val newValue: String) : AddUserEvent
    data class OnPhoneNumberChange(val newValue: String) : AddUserEvent
    data class OnEmailChange(val newValue: String) : AddUserEvent
    data class OnDomicileChange(val newValue: String) : AddUserEvent
    data class OnUserTypeChange(val newValue: String) : AddUserEvent
    data class OnPasswordChange(val newValue: String) : AddUserEvent
    data class OnConfirmPasswordChange(val newValue: String) : AddUserEvent
    data object ClearError : AddUserEvent
    data object SaveUser : AddUserEvent
}

sealed interface AddUserEffect {
    data class ShowToastError(val message: String) : AddUserEffect
    data object NavigateBack : AddUserEffect
}
